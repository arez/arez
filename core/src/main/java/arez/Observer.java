package arez;

import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverDisposeEvent;
import arez.spy.ObserverInfo;
import grim.annotations.OmitSymbol;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
  @OmitSymbol( unless = "arez.enable_native_components" )
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
   * typically correspond to the observables that were accessed in the
   * last transaction that this observer was tracking.
   *
   * <p>This list should contain no duplicates.</p>
   */
  @Nonnull
  private List<ObservableValue<?>> _dependencies = new ArrayList<>();
  /**
   * The list of hooks that this observer that will be invoked when it deactivates or
   * has already been invoked as part of the register/activate process.
   * These correspond to the hooks that were registered in the last
   * transaction that this observer was tracking.
   */
  @Nonnull
  private Map<String, Hook> _hooks = new LinkedHashMap<>();
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
   * This should be null if {@link Arez#areSpiesEnabled()} is false.
   */
  @OmitSymbol( unless = "arez.enable_spies" )
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
          Transaction.Flags.transactionMode( flags ) );
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
        invariant( () -> Transaction.Flags.isTransactionModeValid( flags ),
                   () -> "Arez-0079: Observer named '" + getName() + "' incorrectly specified both READ_ONLY " +
                         "and READ_WRITE transaction mode flags." );
      }
      else
      {
        invariant( () -> !Transaction.Flags.isTransactionModeSpecified( flags ),
                   () -> "Arez-0082: Observer named '" + getName() + "' specified transaction mode '" +
                         Transaction.Flags.getTransactionModeName( flags ) + "' when " +
                         "Arez.enforceTransactionType() is false." );
      }
      invariant( () -> Task.Flags.isPriorityValid( _task.getFlags() ),
                 () -> "Arez-0080: Observer named '" + getName() + "' has invalid priority " +
                       Task.Flags.getPriorityIndex( _task.getFlags() ) + "." );
      invariant( () -> Task.Flags.isRunTypeValid( _task.getFlags() ),
                 () -> "Arez-0081: Observer named '" + getName() + "' incorrectly specified both " +
                       "RUN_NOW and RUN_LATER flags." );
      invariant( () -> 0 == ( flags & Observer.Flags.RUN_NOW ) || null != observe,
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
      assert !( Observer.Flags.RUN_NOW == ( flags & Observer.Flags.RUN_NOW ) &&
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
    return 0 != ( _flags & Observer.Flags.NO_REPORT_RESULT );
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
                               ActionFlags.NO_VERIFY_ACTION_REQUIRED );
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
    }
    if ( Arez.shouldEnforceTransactionType() && Arez.shouldCheckInvariants() )
    {
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
          getComputableValue().completeDeactivate();
        }
        final Map<String, Hook> hooks = getHooks();
        hooks
          .values()
          .stream()
          .map( Hook::getOnDeactivate )
          .filter( Objects::nonNull )
          .forEach( hook -> runHook( hook, ObserverError.ON_DEACTIVATE_ERROR ) );
        hooks.clear();
        clearDependencies();
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
      // start == 0 implies that spy events were enabled as part of observer, and thus we can skip this
      // chain of events
      if ( willPropagateSpyEvents() && 0 != start )
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

        final List<ObservableValue<?>> observableValues = current.getObservableValues();
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
    for ( final ObservableValue<?> dependency : getDependencies() )
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
        for ( final ObservableValue<?> observableValue : getDependencies() )
        {
          if ( observableValue.isComputableValue() )
          {
            final Observer owner = observableValue.getObserver();
            final ComputableValue<?> computableValue = owner.getComputableValue();
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
   * Return the hooks.
   *
   * @return the hooks.
   */
  @Nonnull
  Map<String, Hook> getHooks()
  {
    return _hooks;
  }

  /**
   * Replace the current set of hooks with the supplied hooks.
   *
   * @param hooks the new set of hooks.
   */
  void replaceHooks( @Nonnull final Map<String, Hook> hooks )
  {
    _hooks = Objects.requireNonNull( hooks );
  }

  /**
   * Return the dependencies.
   *
   * @return the dependencies.
   */
  @Nonnull
  List<ObservableValue<?>> getDependencies()
  {
    return _dependencies;
  }

  /**
   * Replace the current set of dependencies with supplied dependencies.
   * This should be the only mechanism via which the dependencies are updated.
   *
   * @param dependencies the new set of dependencies.
   */
  void replaceDependencies( @Nonnull final List<ObservableValue<?>> dependencies )
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
  @OmitSymbol( unless = "arez.enable_spies" )
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

  public static final class Flags
  {
    /**
     * The flag can be passed to actions or observers to force the action to not report result to spy infrastructure.
     */
    public static final int NO_REPORT_RESULT = 1 << 12;
    /**
     * Highest priority.
     * This priority should be used when the observer will dispose or release other reactive elements
     * (and thus remove elements from being scheduled).
     * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
     *
     * @see arez.annotations.Priority#HIGHEST
     * @see arez.spy.Priority#HIGHEST
     * @see Task.Flags#PRIORITY_HIGHEST
     */
    public static final int PRIORITY_HIGHEST = 0b001 << 15;
    /**
     * High priority.
     * To reduce the chance that downstream elements will react multiple times within a single
     * reaction round, this priority should be used when the observer may trigger many downstream
     * reactions.
     * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
     *
     * @see arez.annotations.Priority#HIGH
     * @see arez.spy.Priority#HIGH
     * @see Task.Flags#PRIORITY_HIGH
     */
    public static final int PRIORITY_HIGH = 0b010 << 15;
    /**
     * Normal priority if no other priority otherwise specified.
     * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
     *
     * @see arez.annotations.Priority#NORMAL
     * @see arez.spy.Priority#NORMAL
     * @see Task.Flags#PRIORITY_NORMAL
     */
    public static final int PRIORITY_NORMAL = 0b011 << 15;
    /**
     * Low priority.
     * Usually used to schedule observers that reflect state onto non-reactive
     * application components. i.e. Observers that are used to build html views,
     * perform network operations etc. These reactions are often at low priority
     * to avoid recalculation of dependencies (i.e. {@link ComputableValue}s) triggering
     * this reaction multiple times within a single reaction round.
     * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
     *
     * @see arez.annotations.Priority#LOW
     * @see arez.spy.Priority#LOW
     * @see Task.Flags#PRIORITY_LOW
     */
    public static final int PRIORITY_LOW = 0b100 << 15;
    /**
     * Lowest priority. Use this priority if the observer is a {@link ComputableValue} that
     * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
     * recomputing state that is likely to either be unobserved or recomputed as part of
     * another observers reaction.
     * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
     *
     * @see arez.annotations.Priority#LOWEST
     * @see arez.spy.Priority#LOWEST
     * @see Task.Flags#PRIORITY_LOWEST
     */
    public static final int PRIORITY_LOWEST = 0b101 << 15;
    /**
     * Mask used to extract priority bits.
     */
    public static final int PRIORITY_MASK = 0b111 << 15;
    /**
     * The observer can only read arez state.
     */
    public static final int READ_ONLY = 1 << 24;
    /**
     * The observer can read or write arez state.
     */
    public static final int READ_WRITE = 1 << 23;
    /**
     * The scheduler will be triggered when the observer is created to immediately invoke the
     * {@link Observer#_observe} function. This configuration should not be specified if there
     * is no {@link Observer#_observe} function supplied. This should not be
     * specified if {@link #RUN_LATER} is specified.
     */
    @SuppressWarnings( "WeakerAccess" )
    public static final int RUN_NOW = 1 << 22;
    /**
     * The scheduler will not be triggered when the observer is created. The observer either
     * has no {@link Observer#_observe} function or is responsible for ensuring that
     * {@link ArezContext#triggerScheduler()} is invoked at a later time. This should not be
     * specified if {@link #RUN_NOW} is specified.
     */
    public static final int RUN_LATER = 1 << 21;
    /**
     * Flag indicating that the Observer is allowed to observe {@link ComputableValue} instances with a lower priority.
     */
    public static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = 1 << 30;
    /**
     * Indicates that the an action can be created from within the Observers observed function.
     */
    public static final int NESTED_ACTIONS_ALLOWED = 1 << 29;
    /**
     * Indicates that the an action must not be created from within the Observers observed function.
     */
    public static final int NESTED_ACTIONS_DISALLOWED = 1 << 28;
    /**
     * Flag set set if the application code can not invoke the {@link Observer#reportStale()} method.
     *
     * @see arez.annotations.DepType#AREZ
     */
    public static final int AREZ_DEPENDENCIES = 1 << 27;
    /**
     * Flag set set if the application code can not invokethe  {@link Observer#reportStale()} method to indicate
     * that a dependency has changed. In this scenario it is not an error if the observer does not invoke the
     * {@link ObservableValue#reportObserved()} on a dependency during it's reaction.
     *
     * @see arez.annotations.DepType#AREZ_OR_NONE
     */
    public static final int AREZ_OR_NO_DEPENDENCIES = 1 << 26;
    /**
     * Flag set if the application code can invoke the {@link Observer#reportStale()} method to indicate that a non-arez dependency has changed.
     *
     * @see arez.annotations.DepType#AREZ_OR_EXTERNAL
     */
    public static final int AREZ_OR_EXTERNAL_DEPENDENCIES = 1 << 25;
    /**
     * The runtime will keep the observer reacting to dependencies until disposed. This is the default value for
     * observers that supply a observed function.
     */
    public static final int KEEPALIVE = 1 << 20;
    /**
     * Mask used to extract dependency type bits.
     */
    private static final int DEPENDENCIES_TYPE_MASK =
      AREZ_DEPENDENCIES | AREZ_OR_NO_DEPENDENCIES | AREZ_OR_EXTERNAL_DEPENDENCIES;
    /**
     * Mask to extract "NESTED_ACTIONS" option so can derive default value if required.
     */
    private static final int NESTED_ACTIONS_MASK = NESTED_ACTIONS_ALLOWED | NESTED_ACTIONS_DISALLOWED;
    /**
     * Flag indicating whether next scheduled invocation of {@link Observer} should invoke {@link Observer#_observe} or {@link Observer#_onDepsChange}.
     */
    static final int EXECUTE_OBSERVE_NEXT = 1 << 9;
    /**
     * Mask used to extract state bits.
     * State is the lowest bits as it is the most frequently accessed numeric fields
     * and placing values at lower part of integer avoids a shift.
     */
    static final int STATE_MASK = 0b111;
    /**
     * Mask that identifies the bits associated with runtime configuration.
     */
    static final int RUNTIME_FLAGS_MASK = EXECUTE_OBSERVE_NEXT | STATE_MASK;
    /**
     * The observer has been disposed.
     */
    static final int STATE_DISPOSED = 0b001;
    /**
     * The observer is in the process of being disposed.
     */
    static final int STATE_DISPOSING = 0b010;
    /**
     * The observer is not active and is not holding any data about it's dependencies.
     * Typically mean this tracker observer has not been run or if it is a ComputableValue that
     * there is no observer observing the associated ObservableValue.
     */
    static final int STATE_INACTIVE = 0b011;
    /**
     * No change since last time observer was notified.
     */
    static final int STATE_UP_TO_DATE = 0b100;
    /**
     * A transitive dependency has changed but it has not been determined if a shallow
     * dependency has changed. The observer will need to check if shallow dependencies
     * have changed. Only Derived observables will propagate POSSIBLY_STALE state.
     */
    static final int STATE_POSSIBLY_STALE = 0b101;
    /**
     * A dependency has changed so the observer will need to recompute.
     */
    static final int STATE_STALE = 0b110;
    /**
     * The flag is valid on observers associated with computable values and will deactivate the observer if the
     * computable value has no observers.
     */
    static final int DEACTIVATE_ON_UNOBSERVE = 1 << 19;
    /**
     * The flag is valid on observers where the observed function is invoked by the application.
     */
    static final int APPLICATION_EXECUTOR = 1 << 18;
    /**
     * Mask used to extract react type bits.
     */
    static final int SCHEDULE_TYPE_MASK = KEEPALIVE | DEACTIVATE_ON_UNOBSERVE | APPLICATION_EXECUTOR;
    /**
     * Mask that identifies the bits associated with static configuration.
     */
    static final int CONFIG_FLAGS_MASK =
      PRIORITY_MASK |
      RUN_NOW | RUN_LATER |
      READ_ONLY | READ_WRITE |
      OBSERVE_LOWER_PRIORITY_DEPENDENCIES |
      NESTED_ACTIONS_MASK |
      DEPENDENCIES_TYPE_MASK |
      SCHEDULE_TYPE_MASK |
      NO_REPORT_RESULT;

    /**
     * Extract and return the observer's state.
     *
     * @param flags the flags.
     * @return the state.
     */
    static int getState( final int flags )
    {
      return flags & STATE_MASK;
    }

    /**
     * Return the new value of flags when supplied with specified state.
     *
     * @param flags the flags.
     * @param state the new state.
     * @return the new flags.
     */
    static int setState( final int flags, final int state )
    {
      return ( flags & ~STATE_MASK ) | state;
    }

    /**
     * Return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
     * The inverse of {@link #isNotActive(int)}
     *
     * @param flags the flags to check.
     * @return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
     */
    static boolean isActive( final int flags )
    {
      return getState( flags ) > STATE_INACTIVE;
    }

    /**
     * Return true if the state is INACTIVE, DISPOSING or DISPOSED.
     * The inverse of {@link #isActive(int)}
     *
     * @param flags the flags to check.
     * @return true if the state is INACTIVE, DISPOSING or DISPOSED.
     */
    static boolean isNotActive( final int flags )
    {
      return !isActive( flags );
    }

    /**
     * Return the least stale observer state. if the state is not active
     * then the {@link #STATE_UP_TO_DATE} will be returned.
     *
     * @param flags the flags to check.
     * @return the least stale observer state.
     */
    static int getLeastStaleObserverState( final int flags )
    {
      final int state = getState( flags );
      return state > STATE_INACTIVE ? state : STATE_UP_TO_DATE;
    }

    /**
     * Return the state as a string.
     *
     * @param state the state value. One of the STATE_* constants
     * @return the string describing state.
     */
    @Nonnull
    static String getStateName( final int state )
    {
      assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
      switch ( state )
      {
        case STATE_DISPOSED:
          return "DISPOSED";
        case STATE_DISPOSING:
          return "DISPOSING";
        case STATE_INACTIVE:
          return "INACTIVE";
        case STATE_POSSIBLY_STALE:
          return "POSSIBLY_STALE";
        case STATE_STALE:
          return "STALE";
        case STATE_UP_TO_DATE:
          return "UP_TO_DATE";
        default:
          return "UNKNOWN(" + state + ")";
      }
    }

    static int nestedActionRule( final int flags )
    {
      return Arez.shouldCheckInvariants() ?
             0 != ( flags & NESTED_ACTIONS_MASK ) ? 0 : NESTED_ACTIONS_DISALLOWED :
             0;
    }

    /**
     * Return true if flags contains a valid nested action mode.
     *
     * @param flags the flags.
     * @return true if flags contains valid nested action mode.
     */
    static boolean isNestedActionsModeValid( final int flags )
    {
      return NESTED_ACTIONS_ALLOWED == ( flags & NESTED_ACTIONS_ALLOWED ) ^
             NESTED_ACTIONS_DISALLOWED == ( flags & NESTED_ACTIONS_DISALLOWED );
    }

    /**
     * Return the default dependency type flag if dependency type not specified.
     *
     * @param flags the flags.
     * @return the default dependency type if dependency type unspecified else 0.
     */
    static int dependencyType( final int flags )
    {
      return Arez.shouldCheckInvariants() ? 0 != ( flags & DEPENDENCIES_TYPE_MASK ) ? 0 : AREZ_DEPENDENCIES : 0;
    }

    /**
     * Extract and return the schedule type.
     *
     * @param flags the flags.
     * @return the schedule type.
     */
    static int getScheduleType( final int flags )
    {
      return flags & SCHEDULE_TYPE_MASK;
    }

    /**
     * Return true if flags contains a valid ScheduleType.
     *
     * @param flags the flags.
     * @return true if flags contains a valid ScheduleType.
     */
    static boolean isScheduleTypeValid( final int flags )
    {
      return KEEPALIVE == ( flags & KEEPALIVE ) ^
             DEACTIVATE_ON_UNOBSERVE == ( flags & DEACTIVATE_ON_UNOBSERVE ) ^
             APPLICATION_EXECUTOR == ( flags & APPLICATION_EXECUTOR );
    }
  }
}
