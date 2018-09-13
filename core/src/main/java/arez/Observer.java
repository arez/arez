package arez;

import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverDisposedEvent;
import arez.spy.ObserverInfo;
import arez.spy.Priority;
import arez.spy.ReactionCompletedEvent;
import arez.spy.ReactionStartedEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A node within Arez that is notified of changes in 0 or more Observables.
 */
public final class Observer
  extends Node
{
  /**
   * The component that this observer is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the observer is a "top-level" observer.
   */
  @Nullable
  private final Component _component;
  /**
   * The reference to the ComputedValue if this observer is a derivation.
   */
  @Nullable
  private final ComputedValue<?> _computedValue;
  /**
   * The observables that this observer receives notifications from.
   * These are the dependencies within the dependency graph and will
   * typically correspond to the observables that were accessed in last
   * transaction that this observer was tracking.
   *
   * This list should contain no duplicates.
   */
  @Nonnull
  private ArrayList<ObservableValue<?>> _dependencies = new ArrayList<>();
  /**
   * Observed function to invoke if any.
   * This may be null if external executor is responsible for executing the observed function via
   * methods such as {@link ArezContext#observe(Observer, Function, Object...)}. If this is null then
   * {@link #_onDepsChanged} must not be null.
   */
  @Nullable
  private final Procedure _observed;
  /**
   * Callback invoked when dependencies are updated.
   * This may be null when the observer re-executes the observed function when dependencies change
   * but in that case {@link #_observed} must not be null.
   */
  @Nullable
  private final Procedure _onDepsChanged;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private ObserverInfo _info;
  /**
   * A bitfield that contains config time and runtime flags/state.
   * See the values in {@link Flags} that are covered by the masks
   * {@link Flags#RUNTIME_FLAGS_MASK} and {@link Flags#CONFIG_FLAGS_MASK}
   * for acceptable values.
   */
  private int _flags;

  Observer( @Nonnull final ComputedValue<?> computedValue, final int flags )
  {
    this( Arez.areZonesEnabled() ? computedValue.getContext() : null,
          null,
          Arez.areNamesEnabled() ? computedValue.getName() : null,
          computedValue,
          computedValue::compute,
          null,
          flags |
          ( Flags.KEEPALIVE == Flags.getScheduleType( flags ) ? 0 : Flags.DEACTIVATE_ON_UNOBSERVE ) |
          Flags.runType( flags, Flags.KEEPALIVE == Flags.getScheduleType( flags ) ? Flags.RUN_NOW : Flags.RUN_LATER ) |
          ( Arez.shouldEnforceTransactionType() ? Flags.READ_ONLY : 0 ) |
          Flags.NESTED_ACTIONS_DISALLOWED |
          Flags.priority( flags ) );
  }

  Observer( @Nullable final ArezContext context,
            @Nullable final Component component,
            @Nullable final String name,
            @Nullable final Procedure observed,
            @Nullable final Procedure onDepsChanged,
            final int flags )
  {
    this( context,
          component,
          name,
          null,
          observed,
          onDepsChanged,
          flags |
          ( null == observed ? Flags.SCHEDULED_EXTERNALLY : Flags.KEEPALIVE ) |
          Flags.runType( flags, null == observed ? Flags.RUN_LATER : Flags.RUN_NOW ) |
          Flags.priority( flags ) |
          Flags.nestedActionRule( flags ) |
          Flags.transactionMode( flags ) );
  }

  private Observer( @Nullable final ArezContext context,
                    @Nullable final Component component,
                    @Nullable final String name,
                    @Nullable final ComputedValue<?> computedValue,
                    @Nullable final Procedure observed,
                    @Nullable final Procedure onDepsChanged,
                    final int flags )
  {
    super( context, name );
    _flags = flags | Flags.STATE_INACTIVE;
    if ( Arez.shouldCheckInvariants() )
    {
      if ( Arez.shouldEnforceTransactionType() )
      {
        invariant( () -> Flags.isTransactionModeValid( flags ),
                   () -> "Arez-0079: Observer named '" + getName() + "' incorrectly specified both READ_ONLY " +
                         "and READ_WRITE transaction mode flags." );
      }
      else
      {
        invariant( () -> !Flags.isTransactionModeSpecified( flags ),
                   () -> "Arez-0082: Observer named '" + getName() + "' specified transaction mode '" +
                         Flags.getTransactionModeName( flags ) + "' when Arez.enforceTransactionType() is false." );
      }
      invariant( () -> Flags.isPriorityValid( flags ),
                 () -> "Arez-0080: Observer named '" + getName() + "' has invalid priority " +
                       Flags.getPriorityIndex( flags ) + "." );
      invariant( () -> Flags.isRunTypeValid( flags ),
                 () -> "Arez-0081: Observer named '" + getName() + "' incorrectly specified both " +
                       "RUN_NOW and RUN_LATER flags." );
      invariant( () -> 0 == ( flags & Flags.RUN_NOW ) || null != observed,
                 () -> "Arez-0206: Observer named '" + getName() + "' incorrectly specified " +
                       "RUN_NOW flag but the observed function is null." );
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0083: Observer named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
      invariant( () -> Flags.getPriority( flags ) != Flags.PRIORITY_LOWEST ||
                       0 == ( flags & Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES ),
                 () -> "Arez-0184: Observer named '" + getName() + "' has LOWEST priority but has passed " +
                       "OBSERVE_LOWER_PRIORITY_DEPENDENCIES option which should not be present as the observer " +
                       "has no lower priority." );
      invariant( () -> null != observed || null != onDepsChanged,
                 () -> "Arez-0204: Observer named '" + getName() + "' has not supplied a value for either the " +
                       "observed parameter or the onDepsChanged parameter." );
      // Next lines are impossible situations to create from tests. Add asserts to verify this.
      assert Flags.KEEPALIVE != Flags.getScheduleType( flags ) || null != observed;
      assert Flags.SCHEDULED_EXTERNALLY != Flags.getScheduleType( flags ) || null == observed;
      invariant( () -> !( Flags.RUN_NOW == ( flags & Flags.RUN_NOW ) &&
                          Flags.KEEPALIVE != Flags.getScheduleType( flags ) &&
                          null != computedValue ),
                 () -> "Arez-0208: ComputedValue named '" + getName() + "' incorrectly specified " +
                       "RUN_NOW flag but not the KEEPALIVE flag." );
      invariant( () -> Flags.isNestedActionsModeValid( flags ),
                 () -> "Arez-0209: Observer named '" + getName() + "' incorrectly specified both the " +
                       "NESTED_ACTIONS_ALLOWED flag and the NESTED_ACTIONS_DISALLOWED flag." );
      invariant( () -> Flags.isScheduleTypeValid( flags ),
                 () -> "Arez-0210: Observer named '" + getName() + "' incorrectly specified multiple " +
                       "schedule type flags (KEEPALIVE, DEACTIVATE_ON_UNOBSERVE, SCHEDULED_EXTERNALLY)." );
      invariant( () -> ( ~( Flags.RUNTIME_FLAGS_MASK | Flags.CONFIG_FLAGS_MASK ) & flags ) == 0,
                 () -> "Arez-0207: Observer named '" + getName() + "' specified illegal flags: " +
                       ( ~( Flags.RUNTIME_FLAGS_MASK | Flags.CONFIG_FLAGS_MASK ) & flags ) );
    }
    assert null == computedValue || !Arez.areNamesEnabled() || computedValue.getName().equals( name );
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _computedValue = computedValue;
    _observed = observed;
    _onDepsChanged = onDepsChanged;

    executeObservedNextIfPresent();

    if ( null == _computedValue )
    {
      if ( null != _component )
      {
        _component.addObserver( this );
      }
      else if ( Arez.areRegistriesEnabled() )
      {
        getContext().registerObserver( this );
      }
    }
    if ( null == _computedValue )
    {
      if ( willPropagateSpyEvents() )
      {
        getSpy().reportSpyEvent( new ObserverCreatedEvent( asInfo() ) );
      }
      if ( null != _observed )
      {
        initialSchedule();
      }
    }
  }

  void initialSchedule()
  {
    final ArezContext context = getContext();
    context.scheduleReaction( this );
    if ( 0 != ( _flags & Flags.RUN_NOW ) )
    {
      context.triggerScheduler();
    }
  }

  int getPriorityIndex()
  {
    return Flags.getPriorityIndex( _flags );
  }

  @Nonnull
  Priority getPriority()
  {
    return Priority.values()[ getPriorityIndex() ];
  }

  boolean arezOnlyDependencies()
  {
    assert Arez.shouldCheckApiInvariants();
    return 0 == ( _flags & Flags.NON_AREZ_DEPENDENCIES );
  }

  /**
   * Return true if the Observer supports invocations of {@link #schedule()} from non-arez code.
   * This is true if both a {@link #_observed} and {@link #_onDepsChanged} parameters
   * are provided at construction.
   */
  boolean supportsManualSchedule()
  {
    assert Arez.shouldCheckApiInvariants();
    return null != _observed && null != _onDepsChanged;
  }

  boolean isApplicationExecutor()
  {
    assert Arez.shouldCheckApiInvariants();
    return null == _observed;
  }

  boolean nestedActionsAllowed()
  {
    assert Arez.shouldCheckApiInvariants();
    return 0 != ( _flags & Flags.NESTED_ACTIONS_ALLOWED );
  }

  boolean canObserveLowerPriorityDependencies()
  {
    assert Arez.shouldCheckApiInvariants();
    return 0 != ( _flags & Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
  }

  boolean isComputedValue()
  {
    return null != _computedValue;
  }

  /**
   * Make the Observer INACTIVE and release any resources associated with observer.
   * The applications should NOT interact with the Observer after it has been disposed.
   */
  @Override
  public void dispose()
  {
    if ( !isDisposedOrDisposing() )
    {
      getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null,
                               true,
                               false,
                               this::performDispose );
      if ( !isComputedValue() )
      {
        if ( willPropagateSpyEvents() )
        {
          reportSpyEvent( new ObserverDisposedEvent( asInfo() ) );
        }
        if ( null != _component )
        {
          _component.removeObserver( this );
        }
        else if ( Arez.areRegistriesEnabled() )
        {
          getContext().deregisterObserver( this );
        }
      }
      if ( null != _computedValue )
      {
        _computedValue.dispose();
      }
      markAsDisposed();
    }
  }

  private void performDispose()
  {
    getContext().getTransaction().reportDispose( this );
    markDependenciesLeastStaleObserverAsUpToDate();
    setState( Flags.STATE_DISPOSING );
  }

  void markAsDisposed()
  {
    _flags = Flags.setState( _flags, Flags.STATE_DISPOSED );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return Flags.STATE_DISPOSED == getState();
  }

  boolean isDisposedOrDisposing()
  {
    return Flags.STATE_DISPOSING >= getState();
  }

  /**
   * Return true during invocation of dispose, false otherwise.
   *
   * @return true during invocation of dispose, false otherwise.
   */
  boolean isDisposing()
  {
    return Flags.STATE_DISPOSING == getState();
  }

  /**
   * Return the state of the observer relative to the observers dependencies.
   *
   * @return the state of the observer relative to the observers dependencies.
   */
  int getState()
  {
    return Flags.getState( _flags );
  }

  int getLeastStaleObserverState()
  {
    return Flags.getLeastStaleObserverState( _flags );
  }

  /**
   * Return true if observer creates a READ_WRITE transaction.
   *
   * @return true if observer creates a READ_WRITE transaction.
   */
  boolean isMutation()
  {
    assert Arez.shouldEnforceTransactionType();
    return 0 != ( _flags & Flags.READ_WRITE );
  }

  /**
   * Return true if the observer is active.
   * Being "active" means that the state of the observer is not {@link Flags#STATE_INACTIVE},
   * {@link Flags#STATE_DISPOSING} or {@link Flags#STATE_DISPOSED}.
   *
   * <p>An inactive observer has no dependencies and depending on the type of observer may
   * have other consequences. i.e. An inactive observer will never be scheduled even if it has a
   * reaction.</p>
   *
   * @return true if the Observer is active.
   */
  boolean isActive()
  {
    return Flags.isActive( _flags );
  }

  /**
   * Return true if the observer is not active.
   * The inverse of {@link #isActive()}
   *
   * @return true if the Observer is inactive.
   */
  boolean isInactive()
  {
    return !isActive();
  }

  /**
   * This method should be invoked if the observer has non-arez dependencies and one of
   * these dependencies has been updated. This will mark the observer as stale and reschedule
   * the reaction if necessary. The method must be invoked from within a read-write transaction.
   * the reaction if necessary. The method must be invoked from within a read-write transaction.
   */
  public void reportStale()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> !arezOnlyDependencies(),
                    () -> "Arez-0199: Observer.reportStale() invoked on observer named '" + getName() +
                          "' but arezOnlyDependencies = true." );
      apiInvariant( () -> getContext().isTransactionActive(),
                    () -> "Arez-0200: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when there is no active transaction." );
      apiInvariant( () -> getContext().getTransaction().isMutation(),
                    () -> "Arez-0201: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when the active transaction '" + getContext().getTransaction().getName() +
                          "' is READ_ONLY rather than READ_WRITE." );
    }
    setState( Flags.STATE_STALE );
  }

  /**
   * Set the state of the observer.
   * Call the hook actions for relevant state change.
   * This is equivalent to passing true in <code>schedule</code> parameter to {@link #setState(int, boolean)}
   *
   * @param state the new state of the observer.
   */
  void setState( final int state )
  {
    setState( state, true );
  }

  /**
   * Set the state of the observer.
   * Call the hook actions for relevant state change.
   *
   * @param state    the new state of the observer.
   * @param schedule true if a state transition can cause observer to reschedule, false otherwise.
   */
  void setState( final int state, final boolean schedule )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0086: Attempt to invoke setState on observer named '" + getName() + "' when there is " +
                       "no active transaction." );
      invariantState();
    }
    final int originalState = getState();
    if ( state != originalState )
    {
      _flags = Flags.setState( _flags, state );
      if ( Arez.shouldCheckInvariants() && Flags.STATE_DISPOSED == originalState )
      {
        fail( () -> "Arez-0087: Attempted to activate disposed observer named '" + getName() + "'." );
      }
      else if ( null == _computedValue && Flags.STATE_STALE == state )
      {
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( null != _computedValue &&
                Flags.STATE_UP_TO_DATE == originalState &&
                ( Flags.STATE_STALE == state || Flags.STATE_POSSIBLY_STALE == state ) )
      {
        _computedValue.getObservableValue().reportPossiblyChanged();
        runHook( _computedValue.getOnStale(), ObserverError.ON_STALE_ERROR );
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( Flags.STATE_INACTIVE == state ||
                ( Flags.STATE_INACTIVE != originalState && Flags.STATE_DISPOSING == state ) )
      {
        if ( isComputedValue() )
        {
          final ComputedValue<?> computedValue = getComputedValue();
          runHook( computedValue.getOnDeactivate(), ObserverError.ON_DEACTIVATE_ERROR );
          computedValue.setValue( null );
        }
        clearDependencies();
      }
      else if ( Flags.STATE_INACTIVE == originalState )
      {
        if ( isComputedValue() )
        {
          runHook( getComputedValue().getOnActivate(), ObserverError.ON_ACTIVATE_ERROR );
        }
      }
      if ( Arez.shouldCheckInvariants() )
      {
        invariantState();
      }
    }
  }

  /**
   * Run the supplied hook if non null.
   *
   * @param hook the hook to run.
   */
  void runHook( @Nullable final Procedure hook, @Nonnull final ObserverError error )
  {
    if ( null != hook )
    {
      try
      {
        hook.call();
      }
      catch ( final Throwable t )
      {
        getContext().reportObserverError( this, error, t );
      }
    }
  }

  /**
   * Remove all dependencies, removing this observer from all dependencies in the process.
   */
  void clearDependencies()
  {
    getDependencies().forEach( dependency -> {
      dependency.removeObserver( this );
      if ( !dependency.hasObservers() )
      {
        dependency.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
      }
    } );
    getDependencies().clear();
  }

  /**
   * Return true if this observer has a pending reaction.
   *
   * @return true if this observer has a pending reaction.
   */
  boolean isScheduled()
  {
    return 0 != ( _flags & Flags.SCHEDULED );
  }

  /**
   * Clear the scheduled flag. This is called when the observer's reaction is executed so it can be scheduled again.
   */
  void clearScheduledFlag()
  {
    _flags &= ~Flags.SCHEDULED;
  }

  /**
   * Set the scheduled flag. This is called when the observer is schedule so it can not be scheduled again until it has run.
   */
  void setScheduledFlag()
  {
    _flags |= Flags.SCHEDULED;
  }

  /**
   * Schedule this observer if it does not already have a reaction pending.
   * The observer will not actually react if it is not already marked as STALE.
   */
  public void schedule()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( this::supportsManualSchedule,
                    () -> "Arez-0202: Observer.schedule() invoked on observer named '" + getName() +
                          "' but supportsManualSchedule() returns false." );
    }
    executeObservedNextIfPresent();
    scheduleReaction();
    getContext().triggerScheduler();
  }

  /**
   * Schedule this observer if it does not already have a reaction pending.
   */
  void scheduleReaction()
  {
    if ( isNotDisposed() )
    {
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( this::isActive,
                   () -> "Arez-0088: Observer named '" + getName() + "' is not active but an attempt has been made " +
                         "to schedule observer." );
      }
      if ( !isScheduled() )
      {
        getContext().scheduleReaction( this );
      }
    }
  }

  /**
   * Run the reaction in a transaction with the name and mode defined
   * by the observer. If the reaction throws an exception, the exception is reported
   * to the context global ObserverErrorHandlers
   */
  void invokeReaction()
  {
    if ( isNotDisposed() )
    {
      final long start;
      if ( willPropagateSpyEvents() )
      {
        start = System.currentTimeMillis();
        if ( isComputedValue() )
        {
          reportSpyEvent( new ComputeStartedEvent( getComputedValue().asInfo() ) );
        }
        else
        {
          reportSpyEvent( new ReactionStartedEvent( asInfo() ) );
        }
      }
      else
      {
        start = 0;
      }
      try
      {
        // ComputedValues may have calculated their values and thus be up to date so no need to recalculate.
        if ( Flags.STATE_UP_TO_DATE != getState() )
        {
          if ( shouldExecuteObservedNext() )
          {
            executeOnDepsChangedNextIfPresent();
            runObservedFunction();
          }
          else
          {
            assert null != _onDepsChanged;
            _onDepsChanged.call();
          }
        }
        else if ( shouldExecuteObservedNext() )
        {
          /*
           * The observer should invoke onDepsChanged next if the following conditions hold.
           *  - a manual schedule() invocation
           *  - the observer is not stale, and
           *  - there is an onDepsChanged hook present
           *
           *  This block ensures this is the case.
           */
          executeOnDepsChangedNextIfPresent();
        }
      }
      catch ( final Throwable t )
      {
        getContext().reportObserverError( this, ObserverError.REACTION_ERROR, t );
      }
      if ( willPropagateSpyEvents() )
      {
        final long duration = System.currentTimeMillis() - start;
        if ( isComputedValue() )
        {
          reportSpyEvent( new ComputeCompletedEvent( getComputedValue().asInfo(), duration ) );
        }
        else
        {
          reportSpyEvent( new ReactionCompletedEvent( asInfo(), duration ) );
        }
      }
    }
  }

  private void runObservedFunction()
    throws Throwable
  {
    assert null != _observed;
    final Procedure action;
    if ( Arez.shouldCheckInvariants() && arezOnlyDependencies() )
    {
      action = () -> {
        _observed.call();
        final Transaction current = Transaction.current();

        final ArrayList<ObservableValue<?>> observableValues = current.getObservableValues();
        invariant( () -> Objects.requireNonNull( current.getTracker() ).isDisposing() ||
                         ( null != observableValues && !observableValues.isEmpty() ),
                   () -> "Arez-0172: Observer named '" + getName() + "' that does not use an external executor " +
                         "completed observed funnction but is not observing any properties. As a result the observer " +
                         "will never be rescheduled." );
      };
    }
    else
    {
      action = _observed;
    }
    getContext()._action( Arez.areNamesEnabled() ? getName() : null,
                          Arez.shouldEnforceTransactionType() && isMutation(),
                          false,
                          true,
                          action,
                          this );
  }

  /**
   * Utility to mark all dependencies least stale observer as up to date.
   * Used when the Observer is determined to be up todate.
   */
  void markDependenciesLeastStaleObserverAsUpToDate()
  {
    for ( final ObservableValue dependency : getDependencies() )
    {
      dependency.setLeastStaleObserverState( Flags.STATE_UP_TO_DATE );
    }
  }

  /**
   * Determine if any dependency of the Observer has actually changed.
   * If the state is POSSIBLY_STALE then recalculate any ComputedValue dependencies.
   * If any ComputedValue dependencies actually changed then the STALE state will
   * be propagated.
   *
   * <p>By iterating over the dependencies in the same order that they were reported and
   * stopping on the first change, all the recalculations are only called for ComputedValues
   * that will be tracked by derivation. That is because we assume that if the first N
   * dependencies of the derivation doesn't change then the derivation should run the same way
   * up until accessing N-th dependency.</p>
   *
   * @return true if the Observer should be recomputed.
   */
  boolean shouldCompute()
  {
    final int state = getState();
    switch ( state )
    {
      case Flags.STATE_UP_TO_DATE:
        return false;
      case Flags.STATE_INACTIVE:
      case Flags.STATE_STALE:
        return true;
      case Flags.STATE_POSSIBLY_STALE:
      {
        for ( final ObservableValue observableValue : getDependencies() )
        {
          if ( observableValue.isComputedValue() )
          {
            final Observer owner = observableValue.getObserver();
            final ComputedValue computedValue = owner.getComputedValue();
            try
            {
              computedValue.get();
            }
            catch ( final Throwable ignored )
            {
            }
            // Call to get() will update this state if ComputedValue changed
            if ( Flags.STATE_STALE == getState() )
            {
              return true;
            }
          }
        }
      }
      break;
      default:
        if ( Arez.shouldCheckInvariants() )
        {
          fail( () -> "Arez-0205: Observer.shouldCompute() invoked on observer named '" + getName() +
                      "' but observer is in state " + Flags.getStateName( getState() ) );
        }
    }
    /*
     * This results in POSSIBLY_STALE returning to UP_TO_DATE
     */
    markDependenciesLeastStaleObserverAsUpToDate();
    return false;
  }

  /**
   * Return the dependencies.
   *
   * @return the dependencies.
   */
  @Nonnull
  ArrayList<ObservableValue<?>> getDependencies()
  {
    return _dependencies;
  }

  /**
   * Replace the current set of dependencies with supplied dependencies.
   * This should be the only mechanism via which the dependencies are updated.
   *
   * @param dependencies the new set of dependencies.
   */
  void replaceDependencies( @Nonnull final ArrayList<ObservableValue<?>> dependencies )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariantDependenciesUnique( "Pre replaceDependencies" );
    }
    _dependencies = Objects.requireNonNull( dependencies );
    if ( Arez.shouldCheckInvariants() )
    {
      invariantDependenciesUnique( "Post replaceDependencies" );
      invariantDependenciesBackLink( "Post replaceDependencies" );
      invariantDependenciesNotDisposed();
    }
  }

  /**
   * Ensure the dependencies list contain no duplicates.
   * Should be optimized away if invariant checking is disabled.
   *
   * @param context some useful debugging context used in invariant checks.
   */
  void invariantDependenciesUnique( @Nonnull final String context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getDependencies().size() == new HashSet<>( getDependencies() ).size(),
                 () -> "Arez-0089: " + context + ": The set of dependencies in observer named '" +
                       getName() + "' is not unique. Current list: '" +
                       getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ) + "'." );
    }
  }

  /**
   * Ensure all dependencies contain this observer in the list of observers.
   * Should be optimized away if invariant checking is disabled.
   *
   * @param context some useful debugging context used in invariant checks.
   */
  void invariantDependenciesBackLink( @Nonnull final String context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      getDependencies().forEach( observable ->
                                   invariant( () -> observable.getObservers().contains( this ),
                                              () -> "Arez-0090: " + context + ": Observer named '" + getName() +
                                                    "' has ObservableValue dependency named '" + observable.getName() +
                                                    "' which does not contain the observer in the list of " +
                                                    "observers." ) );
      invariantComputedValueObserverState();
    }
  }

  /**
   * Ensure all dependencies are not disposed.
   */
  void invariantDependenciesNotDisposed()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      getDependencies().forEach( observable ->
                                   invariant( observable::isNotDisposed,
                                              () -> "Arez-0091: Observer named '" + getName() + "' has " +
                                                    "ObservableValue dependency named '" + observable.getName() +
                                                    "' which is disposed." ) );
      invariantComputedValueObserverState();
    }
  }

  /**
   * Ensure that state field and other fields of the Observer are consistent.
   */
  void invariantState()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      if ( isInactive() && !isDisposing() )
      {
        invariant( () -> getDependencies().isEmpty(),
                   () -> "Arez-0092: Observer named '" + getName() + "' is inactive but still has dependencies: " +
                         getDependencies().stream().map( Node::getName ).collect( Collectors.toList() ) + "." );
      }
      if ( null != _computedValue && _computedValue.isNotDisposed() )
      {
        final ObservableValue<?> observable = _computedValue.getObservableValue();
        invariant( () -> Objects.equals( observable.isComputedValue() ? observable.getObserver() : null, this ),
                   () -> "Arez-0093: Observer named '" + getName() + "' is associated with an ObservableValue that " +
                         "does not link back to observer." );
      }
    }
  }

  void invariantComputedValueObserverState()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      if ( isComputedValue() && isActive() && isNotDisposed() )
      {
        invariant( () -> !getComputedValue().getObservableValue().getObservers().isEmpty() ||
                         Objects.equals( getContext().getTransaction().getTracker(), this ),
                   () -> "Arez-0094: Observer named '" + getName() + "' is a ComputedValue and active but the " +
                         "associated ObservableValue has no observers." );
      }
    }
  }

  /**
   * Return the ComputedValue for Observer.
   * This should not be called if observer is not a derivation and will generate an invariant failure
   * if invariants are enabled.
   *
   * @return the associated ComputedValue.
   */
  @Nonnull
  ComputedValue<?> getComputedValue()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isComputedValue,
                 () -> "Arez-0095: Attempted to invoke getComputedValue on observer named '" + getName() + "' when " +
                       "is not a computed observer." );
    }
    assert null != _computedValue;
    return _computedValue;
  }

  @Nullable
  Component getComponent()
  {
    return _component;
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @Nonnull
  ObserverInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0197: Observer.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new ObserverInfoImpl( getContext().getSpy(), this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  @Nullable
  Procedure getObserved()
  {
    return _observed;
  }

  @Nullable
  Procedure getOnDepsChanged()
  {
    return _onDepsChanged;
  }

  boolean isKeepAlive()
  {
    return Flags.KEEPALIVE == Flags.getScheduleType( _flags );
  }

  boolean shouldExecuteObservedNext()
  {
    return 0 != ( _flags & Flags.EXECUTE_OBSERVED_NEXT );
  }

  void executeObservedNextIfPresent()
  {
    if ( null != _observed )
    {
      _flags |= Flags.EXECUTE_OBSERVED_NEXT;
    }
  }

  private void executeOnDepsChangedNextIfPresent()
  {
    if ( null != _onDepsChanged )
    {
      _flags &= ~Flags.EXECUTE_OBSERVED_NEXT;
    }
  }
}
