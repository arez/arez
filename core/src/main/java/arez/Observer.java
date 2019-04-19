package arez;

import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverDisposeEvent;
import arez.spy.ObserverInfo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static arez.Guards.*;

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
   * The reference to the ComputableValue if this observer is a derivation.
   */
  @Nullable
  private final ComputableValue<?> _computableValue;
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
   * Observe function to invoke if any.
   * This may be null if external executor is responsible for executing the observe function via
   * methods such as {@link ArezContext#observe(Observer, Function, Object...)}. If this is null then
   * {@link #_onDepsChange} must not be null.
   */
  @Nullable
  private final Procedure _observe;
  /**
   * Callback invoked when dependencies are updated.
   * This may be null when the observer re-executes the observe function when dependencies change
   * but in that case {@link #_observe} must not be null.
   */
  @Nullable
  private final Procedure _onDepsChange;
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
  @Nonnull
  private final Task _task;

  Observer( @Nonnull final ComputableValue<?> computableValue, final int flags )
  {
    this( Arez.areZonesEnabled() ? computableValue.getContext() : null,
          null,
          Arez.areNamesEnabled() ? computableValue.getName() : null,
          computableValue,
          computableValue::compute,
          null,
          flags |
          ( Flags.KEEPALIVE == Flags.getScheduleType( flags ) ? 0 : Flags.DEACTIVATE_ON_UNOBSERVE ) |
          Task.Flags.runType( flags, Flags.KEEPALIVE == Flags.getScheduleType( flags ) ?
                                     Task.Flags.RUN_NOW :
                                     Task.Flags.RUN_LATER ) |
          ( Arez.shouldEnforceTransactionType() ? Flags.READ_ONLY : 0 ) |
          Flags.NESTED_ACTIONS_DISALLOWED |
          Flags.dependencyType( flags ) );
  }

  Observer( @Nullable final ArezContext context,
            @Nullable final Component component,
            @Nullable final String name,
            @Nullable final Procedure observe,
            @Nullable final Procedure onDepsChange,
            final int flags )
  {
    this( context,
          component,
          name,
          null,
          observe,
          onDepsChange,
          flags |
          ( null == observe ? Flags.APPLICATION_EXECUTOR : Flags.KEEPALIVE ) |
          Task.Flags.runType( flags, null == observe ? Task.Flags.RUN_LATER : Task.Flags.RUN_NOW ) |
          Flags.nestedActionRule( flags ) |
          Flags.dependencyType( flags ) |
          Flags.transactionMode( flags ) );
  }

  private Observer( @Nullable final ArezContext context,
                    @Nullable final Component component,
                    @Nullable final String name,
                    @Nullable final ComputableValue<?> computableValue,
                    @Nullable final Procedure observe,
                    @Nullable final Procedure onDepsChange,
                    final int flags )
  {
    super( context, name );
    _task = new Task( context,
                      name,
                      this::invokeReaction,
                      ( flags & Task.Flags.OBSERVER_TASK_FLAGS_MASK ) |
                      Task.Flags.NO_REGISTER_TASK |
                      Task.Flags.NO_WRAP_TASK );
    _flags = ( flags & ~Task.Flags.OBSERVER_TASK_FLAGS_MASK ) | Flags.STATE_INACTIVE;
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
      invariant( () -> Task.Flags.isPriorityValid( _task.getFlags() ),
                 () -> "Arez-0080: Observer named '" + getName() + "' has invalid priority " +
                       Task.Flags.getPriorityIndex( _task.getFlags() ) + "." );
      invariant( () -> Task.Flags.isRunTypeValid( _task.getFlags() ),
                 () -> "Arez-0081: Observer named '" + getName() + "' incorrectly specified both " +
                       "RUN_NOW and RUN_LATER flags." );
      invariant( () -> 0 == ( flags & Flags.RUN_NOW ) || null != observe,
                 () -> "Arez-0206: Observer named '" + getName() + "' incorrectly specified " +
                       "RUN_NOW flag but the observe function is null." );
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0083: Observer named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
      invariant( () -> Task.Flags.getPriority( flags ) != Task.Flags.PRIORITY_LOWEST ||
                       0 == ( flags & Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES ),
                 () -> "Arez-0184: Observer named '" + getName() + "' has LOWEST priority but has passed " +
                       "OBSERVE_LOWER_PRIORITY_DEPENDENCIES option which should not be present as the observer " +
                       "has no lower priority." );
      invariant( () -> null != observe || null != onDepsChange,
                 () -> "Arez-0204: Observer named '" + getName() + "' has not supplied a value for either the " +
                       "observe parameter or the onDepsChange parameter." );
      // Next lines are impossible situations to create from tests. Add asserts to verify this.
      assert Flags.KEEPALIVE != Flags.getScheduleType( flags ) || null != observe;
      assert Flags.APPLICATION_EXECUTOR != Flags.getScheduleType( flags ) || null == observe;
      assert !( Flags.RUN_NOW == ( flags & Flags.RUN_NOW ) &&
                Flags.KEEPALIVE != Flags.getScheduleType( flags ) &&
                null != computableValue );
      invariant( () -> Flags.isNestedActionsModeValid( flags ),
                 () -> "Arez-0209: Observer named '" + getName() + "' incorrectly specified both the " +
                       "NESTED_ACTIONS_ALLOWED flag and the NESTED_ACTIONS_DISALLOWED flag." );
      invariant( () -> Flags.isScheduleTypeValid( flags ),
                 () -> "Arez-0210: Observer named '" + getName() + "' incorrectly specified multiple " +
                       "schedule type flags (KEEPALIVE, DEACTIVATE_ON_UNOBSERVE, APPLICATION_EXECUTOR)." );
      invariant( () -> ( ~( Flags.RUNTIME_FLAGS_MASK | Flags.CONFIG_FLAGS_MASK ) & flags ) == 0,
                 () -> "Arez-0207: Observer named '" + getName() + "' specified illegal flags: " +
                       ( ~( Flags.RUNTIME_FLAGS_MASK | Flags.CONFIG_FLAGS_MASK ) & flags ) );
    }
    assert null == computableValue || !Arez.areNamesEnabled() || computableValue.getName().equals( name );
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _computableValue = computableValue;
    _observe = observe;
    _onDepsChange = onDepsChange;

    executeObserveNextIfPresent();

    if ( null == _computableValue )
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
    if ( null == _computableValue )
    {
      if ( willPropagateSpyEvents() )
      {
        getSpy().reportSpyEvent( new ObserverCreateEvent( asInfo() ) );
      }
      if ( null != _observe )
      {
        initialSchedule();
      }
    }
  }

  void initialSchedule()
  {
    getContext().scheduleReaction( this );
    _task.triggerSchedulerInitiallyUnlessRunLater();
  }

  boolean areArezDependenciesRequired()
  {
    assert Arez.shouldCheckApiInvariants();
    return Flags.AREZ_DEPENDENCIES == ( _flags & Flags.AREZ_DEPENDENCIES );
  }

  boolean areExternalDependenciesAllowed()
  {
    assert Arez.shouldCheckApiInvariants();
    return Flags.AREZ_OR_EXTERNAL_DEPENDENCIES == ( _flags & Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );
  }

  /**
   * Return true if the Observer supports invocations of {@link #schedule()} from non-arez code.
   * This is true if both a {@link #_observe} and {@link #_onDepsChange} parameters
   * are provided at construction.
   */
  boolean supportsManualSchedule()
  {
    assert Arez.shouldCheckApiInvariants();
    return null != _observe && null != _onDepsChange;
  }

  boolean isApplicationExecutor()
  {
    assert Arez.shouldCheckApiInvariants();
    return null == _observe;
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

  boolean noReportResults()
  {
    assert Arez.areSpiesEnabled();
    return 0 != ( _flags & Flags.NO_REPORT_RESULT );
  }

  boolean isComputableValue()
  {
    return null != _computableValue;
  }

  /**
   * Make the Observer INACTIVE and release any resources associated with observer.
   * The applications should NOT interact with the Observer after it has been disposed.
   */
  @Override
  public void dispose()
  {
    if ( isNotDisposedOrDisposing() )
    {
      getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null,
                               this::performDispose,
                               Flags.NO_VERIFY_ACTION_REQUIRED );
      if ( !isComputableValue() )
      {
        if ( willPropagateSpyEvents() )
        {
          reportSpyEvent( new ObserverDisposeEvent( asInfo() ) );
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
      if ( null != _computableValue )
      {
        _computableValue.dispose();
      }
      _task.dispose();
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

  @Override
  public boolean isDisposed()
  {
    return Flags.STATE_DISPOSED == getState();
  }

  boolean isNotDisposedOrDisposing()
  {
    return Flags.STATE_DISPOSING < getState();
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
      apiInvariant( this::areExternalDependenciesAllowed,
                    () -> "Arez-0199: Observer.reportStale() invoked on observer named '" + getName() +
                          "' but the observer has not specified AREZ_OR_EXTERNAL_DEPENDENCIES flag." );
      apiInvariant( () -> getContext().isTransactionActive(),
                    () -> "Arez-0200: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when there is no active transaction." );
      apiInvariant( () -> getContext().getTransaction().isMutation(),
                    () -> "Arez-0201: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when the active transaction '" + getContext().getTransaction().getName() +
                          "' is READ_ONLY rather than READ_WRITE." );
      getContext().getTransaction().markTransactionAsUsed();
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
      else if ( null == _computableValue && Flags.STATE_STALE == state )
      {
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( null != _computableValue &&
                Flags.STATE_UP_TO_DATE == originalState &&
                ( Flags.STATE_STALE == state || Flags.STATE_POSSIBLY_STALE == state ) )
      {
        _computableValue.getObservableValue().reportPossiblyChanged();
        runHook( _computableValue.getOnStale(), ObserverError.ON_STALE_ERROR );
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( Flags.STATE_INACTIVE == state ||
                ( Flags.STATE_INACTIVE != originalState && Flags.STATE_DISPOSING == state ) )
      {
        if ( isComputableValue() )
        {
          final ComputableValue<?> computableValue = getComputableValue();
          runHook( computableValue.getOnDeactivate(), ObserverError.ON_DEACTIVATE_ERROR );
          computableValue.completeDeactivate();
        }
        clearDependencies();
      }
      else if ( Flags.STATE_INACTIVE == originalState &&
                ( ( Flags.STATE_DISPOSED | Flags.STATE_DISPOSING ) & state ) == 0 )
      {
        if ( isComputableValue() )
        {
          runHook( getComputableValue().getOnActivate(), ObserverError.ON_ACTIVATE_ERROR );
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
   * Return the task associated with the observer.
   * The task is used during scheduling.
   *
   * @return the task associated with the observer.
   */
  @Nonnull
  Task getTask()
  {
    return _task;
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
    if ( Arez.shouldEnforceTransactionType() && getContext().isTransactionActive() && Arez.shouldCheckInvariants() )
    {
      getContext().getTransaction().markTransactionAsUsed();
    }
    executeObserveNextIfPresent();
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
      if ( !getTask().isQueued() )
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
        if ( isComputableValue() )
        {
          reportSpyEvent( new ComputeStartEvent( getComputableValue().asInfo() ) );
        }
        else
        {
          reportSpyEvent( new ObserveStartEvent( asInfo() ) );
        }
      }
      else
      {
        start = 0;
      }
      Throwable error = null;
      try
      {
        // ComputableValues may have calculated their values and thus be up to date so no need to recalculate.
        if ( Flags.STATE_UP_TO_DATE != getState() )
        {
          if ( shouldExecuteObserveNext() )
          {
            executeOnDepsChangeNextIfPresent();
            runObserveFunction();
          }
          else
          {
            assert null != _onDepsChange;
            _onDepsChange.call();
          }
        }
        else if ( shouldExecuteObserveNext() )
        {
          /*
           * The observer should invoke onDepsChange next if the following conditions hold.
           *  - a manual schedule() invocation
           *  - the observer is not stale, and
           *  - there is an onDepsChange hook present
           *
           *  This block ensures this is the case.
           */
          executeOnDepsChangeNextIfPresent();
        }
      }
      catch ( final Throwable t )
      {
        error = t;
        getContext().reportObserverError( this, ObserverError.REACTION_ERROR, t );
      }
      if ( willPropagateSpyEvents() )
      {
        final long duration = System.currentTimeMillis() - start;
        if ( isComputableValue() )
        {
          final ComputableValue<?> computableValue = getComputableValue();
          reportSpyEvent( new ComputeCompleteEvent( computableValue.asInfo(),
                                                    noReportResults() ? null : computableValue.getValue(),
                                                    computableValue.getError(),
                                                    (int) duration ) );
        }
        else
        {
          reportSpyEvent( new ObserveCompleteEvent( asInfo(), error, (int) duration ) );
        }
      }
    }
  }

  private void runObserveFunction()
    throws Throwable
  {
    assert null != _observe;
    final Procedure action;
    if ( Arez.shouldCheckInvariants() && areArezDependenciesRequired() )
    {
      action = () -> {
        _observe.call();
        final Transaction current = Transaction.current();

        final ArrayList<ObservableValue<?>> observableValues = current.getObservableValues();
        invariant( () -> Objects.requireNonNull( current.getTracker() ).isDisposing() ||
                         ( null != observableValues && !observableValues.isEmpty() ),
                   () -> "Arez-0172: Observer named '" + getName() + "' that does not use an external executor " +
                         "completed observe function but is not observing any properties. As a result the observer " +
                         "will never be rescheduled." );
      };
    }
    else
    {
      action = _observe;
    }
    getContext().rawObserve( this, action, null );
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
   * If the state is POSSIBLY_STALE then recalculate any ComputableValue dependencies.
   * If any ComputableValue dependencies actually changed then the STALE state will
   * be propagated.
   *
   * <p>By iterating over the dependencies in the same order that they were reported and
   * stopping on the first change, all the recalculations are only called for ComputableValues
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
          if ( observableValue.isComputableValue() )
          {
            final Observer owner = observableValue.getObserver();
            final ComputableValue computableValue = owner.getComputableValue();
            try
            {
              computableValue.get();
            }
            catch ( final Throwable ignored )
            {
            }
            // Call to get() will update this state if ComputableValue changed
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
      invariantComputableValueObserverState();
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
      invariantComputableValueObserverState();
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
      if ( null != _computableValue && _computableValue.isNotDisposed() )
      {
        final ObservableValue<?> observable = _computableValue.getObservableValue();
        invariant( () -> Objects.equals( observable.isComputableValue() ? observable.getObserver() : null, this ),
                   () -> "Arez-0093: Observer named '" + getName() + "' is associated with an ObservableValue that " +
                         "does not link back to observer." );
      }
    }
  }

  void invariantComputableValueObserverState()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      if ( isComputableValue() && isActive() && isNotDisposed() )
      {
        invariant( () -> !getComputableValue().getObservableValue().getObservers().isEmpty() ||
                         Objects.equals( getContext().getTransaction().getTracker(), this ),
                   () -> "Arez-0094: Observer named '" + getName() + "' is a ComputableValue and active but the " +
                         "associated ObservableValue has no observers." );
      }
    }
  }

  /**
   * Return the ComputableValue for Observer.
   * This should not be called if observer is not part of a ComputableValue and will generate an invariant failure
   * if invariants are enabled.
   *
   * @return the associated ComputableValue.
   */
  @Nonnull
  ComputableValue<?> getComputableValue()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isComputableValue,
                 () -> "Arez-0095: Attempted to invoke getComputableValue on observer named '" + getName() + "' when " +
                       "is not a computable observer." );
    }
    assert null != _computableValue;
    return _computableValue;
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
  Procedure getObserve()
  {
    return _observe;
  }

  @Nullable
  Procedure getOnDepsChange()
  {
    return _onDepsChange;
  }

  boolean isKeepAlive()
  {
    return Flags.KEEPALIVE == Flags.getScheduleType( _flags );
  }

  boolean shouldExecuteObserveNext()
  {
    return 0 != ( _flags & Flags.EXECUTE_OBSERVE_NEXT );
  }

  void executeObserveNextIfPresent()
  {
    if ( null != _observe )
    {
      _flags |= Flags.EXECUTE_OBSERVE_NEXT;
    }
  }

  private void executeOnDepsChangeNextIfPresent()
  {
    if ( null != _onDepsChange )
    {
      _flags &= ~Flags.EXECUTE_OBSERVE_NEXT;
    }
  }
}
