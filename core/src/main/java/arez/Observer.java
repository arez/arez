package arez;

import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ObserverDisposedEvent;
import arez.spy.ObserverInfo;
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
   * Hook action called when the Observer is disposed.
   */
  @Nullable
  private Procedure _onDispose;
  /**
   * The state of the observer relative to the observers dependencies.
   */
  @Nonnull
  private ObserverState _state = ObserverState.INACTIVE;
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
   * Flag indicating whether this observer has been scheduled.
   * Should always be false unless _reaction is non-null.
   */
  private boolean _scheduled;
  /**
   * Flag indicating whether next scheduled invocation should invokeReaction {@link #_tracked} or {@link #_onDepsUpdated}.
   */
  private boolean _executeTrackedNext;
  /**
   * The transaction mode in which the observer executes.
   */
  @Nullable
  private final TransactionMode _mode;
  /**
   * Observed executable to invokeReaction if any.
   * This may be null if external scheduler is responsible for executing the tracked executable via
   * methods such as {@link ArezContext#track(Observer, Function, Object...)}. If this is null then
   * {@link #_onDepsUpdated} must not be null.
   */
  @Nullable
  private final Procedure _tracked;
  /**
   * Callback invoked when dependencies are updated.
   * This may be null when the observer re-executes the tracked executable when dependencies change
   * bu in that case {@link #_tracked} must not be null.
   */
  @Nullable
  private final Procedure _onDepsUpdated;
  /**
   * The priority of the observer.
   */
  @Nonnull
  private final Priority _priority;
  /**
   * Flag set to true if the Observer is allowed to observe {@link ComputedValue} instances with a lower priority.
   */
  private final boolean _observeLowerPriorityDependencies;
  /**
   * Flag set to true if the Observer allows nested actions.
   */
  private final boolean _canNestActions;
  /**
   * Flag set to true if the Observer allows nested actions.
   */
  private final boolean _arezOnlyDependencies;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private ObserverInfo _info;

  Observer( @Nullable final ArezContext context,
            @Nullable final Component component,
            @Nullable final String name,
            @Nullable final ComputedValue<?> computedValue,
            @Nullable final TransactionMode mode,
            @Nullable final Procedure tracked,
            @Nullable final Procedure onDepsUpdated,
            @Nonnull final Priority priority,
            final boolean observeLowerPriorityDependencies,
            final boolean canNestActions,
            final boolean arezOnlyDependencies )
  {
    super( context, name );
    if ( Arez.shouldCheckInvariants() )
    {
      if ( Arez.shouldEnforceTransactionType() )
      {
        if ( TransactionMode.READ_WRITE_OWNED == mode )
        {
          invariant( () -> null != computedValue,
                     () -> "Arez-0079: Attempted to construct an observer named '" + getName() + "' with " +
                           "READ_WRITE_OWNED transaction mode but no ComputedValue." );
          assert null != computedValue;
          invariant( () -> null == onDepsUpdated,
                     () -> "Arez-0080: Attempted to construct an ComputedValue '" + getName() + "' that has " +
                           "onDepsUpdated hook." );
        }
        else if ( null != computedValue )
        {
          fail( () -> "Arez-0081: Attempted to construct an observer named '" + getName() + "' with " + mode +
                      " transaction mode and a ComputedValue." );
        }
      }
      else
      {
        invariant( () -> null == mode,
                   () -> "Arez-0082: Observer named '" + getName() + "' specified mode '" + mode + "' when " +
                         "Arez.enforceTransactionType() is false." );
        assert null == mode;
      }
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0083: Observer named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
      invariant( () -> Priority.LOWEST != priority || !observeLowerPriorityDependencies,
                 () -> "Arez-0184: Observer named '" + getName() + "' has LOWEST priority but has passed " +
                       "observeLowerPriorityDependencies = true which should be false as no lower priority." );
      invariant( () -> null != tracked || null != onDepsUpdated,
                 () -> "Arez-0204: Observer named '" + getName() + "' has not supplied a value for either the " +
                       "tracked parameter or the onDepsUpdated parameter." );
    }
    assert null == computedValue || !Arez.areNamesEnabled() || computedValue.getName().equals( name );
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _computedValue = computedValue;
    _mode = Arez.shouldEnforceTransactionType() ? Objects.requireNonNull( mode ) : null;
    _tracked = tracked;
    _onDepsUpdated = onDepsUpdated;
    _priority = Objects.requireNonNull( priority );
    _observeLowerPriorityDependencies = Arez.shouldCheckInvariants() && observeLowerPriorityDependencies;
    _canNestActions = Arez.shouldCheckApiInvariants() && canNestActions;
    _arezOnlyDependencies = Arez.shouldCheckApiInvariants() && arezOnlyDependencies;
    _executeTrackedNext = null != _tracked;
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
  }

  @Nonnull
  Priority getPriority()
  {
    return _priority;
  }

  boolean arezOnlyDependencies()
  {
    return _arezOnlyDependencies;
  }

  /**
   * Return true if the Observer supports invocations of {@link #schedule()} from non-arez code.
   * This is true if both a {@link #_tracked} and {@link #_onDepsUpdated} parameters
   * are provided at construction.
   */
  boolean supportsManualSchedule()
  {
    return Arez.shouldCheckApiInvariants() && null != _tracked && null != _onDepsUpdated;
  }

  boolean isTrackingExecutableExternal()
  {
    return null == _tracked;
  }

  boolean canNestActions()
  {
    return _canNestActions;
  }

  boolean canObserveLowerPriorityDependencies()
  {
    return _observeLowerPriorityDependencies;
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
      _state = ObserverState.DISPOSED;
    }
  }

  private void performDispose()
  {
    getContext().getTransaction().reportDispose( this );
    markDependenciesLeastStaleObserverAsUpToDate();
    setState( ObserverState.DISPOSING );
    runHook( getOnDispose(), ObserverError.ON_DISPOSE_ERROR );
  }

  void markAsDisposed()
  {
    _state = ObserverState.DISPOSED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return ObserverState.DISPOSED == _state;
  }

  boolean isDisposedOrDisposing()
  {
    return ObserverState.DISPOSING.ordinal() >= _state.ordinal();
  }

  /**
   * Return true during invocation of dispose, false otherwise.
   *
   * @return true during invocation of dispose, false otherwise.
   */
  boolean isDisposing()
  {
    return ObserverState.DISPOSING == _state;
  }

  /**
   * Return the state of the observer.
   *
   * @return the state of the observer.
   */
  @Nonnull
  ObserverState getState()
  {
    return _state;
  }

  /**
   * Return the transaction mode in which the observer executes.
   *
   * @return the transaction mode in which the observer executes.
   */
  @Nullable
  TransactionMode getMode()
  {
    assert Arez.shouldEnforceTransactionType();
    return _mode;
  }

  /**
   * Return true if the observer is active.
   * Being "active" means that the state of the observer is not {@link ObserverState#INACTIVE},
   * {@link ObserverState#DISPOSING} or {@link ObserverState#DISPOSED}.
   *
   * <p>An inactive observer has no dependencies and depending on the type of observer may
   * have other consequences. i.e. An inactive observer will never be scheduled even if it has a
   * reaction.</p>
   *
   * @return true if the Observer is active.
   */
  boolean isActive()
  {
    return ObserverState.isActive( getState() );
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
      apiInvariant( () -> !_arezOnlyDependencies,
                    () -> "Arez-0199: Observer.reportStale() invoked on observer named '" + getName() +
                          "' but arezOnlyDependencies = true." );
      apiInvariant( () -> getContext().isTransactionActive(),
                    () -> "Arez-0200: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when there is no active transaction." );
      apiInvariant( () -> TransactionMode.READ_WRITE == getContext().getTransaction().getMode(),
                    () -> "Arez-0201: Observer.reportStale() invoked on observer named '" + getName() +
                          "' when the active transaction '" + getContext().getTransaction().getName() +
                          "' is " + getContext().getTransaction().getMode() + " rather than READ_WRITE." );
    }
    setState( ObserverState.STALE );
  }

  /**
   * Set the state of the observer.
   * Call the hook actions for relevant state change.
   * This is equivalent to passing true in <code>schedule</code> parameter to {@link #setState(ObserverState, boolean)}
   *
   * @param state the new state of the observer.
   */
  void setState( @Nonnull final ObserverState state )
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
  void setState( @Nonnull final ObserverState state, final boolean schedule )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0086: Attempt to invoke setState on observer named '" + getName() + "' when there is " +
                       "no active transaction." );
      invariantState();
    }
    if ( !state.equals( _state ) )
    {
      final ObserverState originalState = _state;
      _state = state;
      if ( Arez.shouldCheckInvariants() && ObserverState.DISPOSED == originalState )
      {
        fail( () -> "Arez-0087: Attempted to activate disposed observer named '" + getName() + "'." );
      }
      else if ( null == _computedValue && ObserverState.STALE == state )
      {
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( null != _computedValue &&
                ObserverState.UP_TO_DATE == originalState &&
                ( ObserverState.STALE == state || ObserverState.POSSIBLY_STALE == state ) )
      {
        _computedValue.getObservableValue().reportPossiblyChanged();
        runHook( _computedValue.getOnStale(), ObserverError.ON_STALE_ERROR );
        if ( schedule )
        {
          scheduleReaction();
        }
      }
      else if ( ObserverState.INACTIVE == _state ||
                ( ObserverState.INACTIVE != originalState && ObserverState.DISPOSING == _state ) )
      {
        if ( isComputedValue() )
        {
          final ComputedValue<?> computedValue = getComputedValue();
          runHook( computedValue.getOnDeactivate(), ObserverError.ON_DEACTIVATE_ERROR );
          computedValue.setValue( null );
        }
        clearDependencies();
      }
      else if ( ObserverState.INACTIVE == originalState )
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
   * Set the onDispose hook.
   *
   * @param onDispose the hook.
   */
  void setOnDispose( @Nullable final Procedure onDispose )
  {
    _onDispose = onDispose;
  }

  /**
   * Return the onDispose hook.
   *
   * @return the onDispose hook.
   */
  @Nullable
  Procedure getOnDispose()
  {
    return _onDispose;
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
        dependency.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
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
    return _scheduled;
  }

  /**
   * Clear the scheduled flag. This is called when the observer's reaction is executed so it can be scheduled again.
   */
  void clearScheduledFlag()
  {
    _scheduled = false;
  }

  /**
   * Set the scheduled flag. This is called when the observer is schedule so it can not be scheduled again until it has run.
   */
  void setScheduledFlag()
  {
    _scheduled = true;
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
    _executeTrackedNext = null != _tracked;
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
        if ( ObserverState.UP_TO_DATE != getState() )
        {
          if ( _executeTrackedNext )
          {
            _executeTrackedNext = null == _onDepsUpdated;
            runTrackedExecutable();
          }
          else
          {
            assert null != _onDepsUpdated;
            _onDepsUpdated.call();
          }
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

  private void runTrackedExecutable()
    throws Throwable
  {
    assert null != _tracked;
    final Procedure action;
    if ( Arez.shouldCheckInvariants() && arezOnlyDependencies() )
    {
      action = () -> {
        _tracked.call();
        final Transaction current = Transaction.current();

        final ArrayList<ObservableValue<?>> observableValues = current.getObservableValues();
        invariant( () -> Objects.requireNonNull( current.getTracker() ).isDisposing() ||
                         ( null != observableValues && !observableValues.isEmpty() ),
                   () -> "Arez-0172: Autorun observer named '" + getName() + "' completed " +
                         "reaction but is not observing any properties. As a result the observer will never " +
                         "be rescheduled. This may not be an autorun candidate." );
      };
    }
    else
    {
      action = _tracked;
    }
    getContext().action( Arez.areNamesEnabled() ? getName() : null,
                         Arez.shouldEnforceTransactionType() ? getMode() : null,
                         false,
                         true,
                         action,
                         null == _computedValue,
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
      dependency.setLeastStaleObserverState( ObserverState.UP_TO_DATE );
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
    switch ( getState() )
    {
      case UP_TO_DATE:
        return false;
      case INACTIVE:
      case STALE:
        return true;
      case POSSIBLY_STALE:
      {
        for ( final ObservableValue observableValue : getDependencies() )
        {
          if ( observableValue.hasOwner() )
          {
            final Observer owner = observableValue.getOwner();
            final ComputedValue computedValue = owner.getComputedValue();
            try
            {
              computedValue.get();
            }
            catch ( final Throwable ignored )
            {
            }
            // Call to get() will update this state if ComputedValue changed
            if ( ObserverState.STALE == getState() )
            {
              return true;
            }
          }
        }
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
        final ObservableValue<?> observableValue = _computedValue.getObservableValue();
        invariant( () -> Objects.equals( observableValue.hasOwner() ? observableValue.getOwner() : null, this ),
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

  void markAsScheduled()
  {
    _scheduled = true;
  }

  @Nullable
  Procedure getTracked()
  {
    return _tracked;
  }

  @Nullable
  Procedure getOnDepsUpdated()
  {
    return _onDepsUpdated;
  }

  boolean shouldExecuteTrackedNext()
  {
    return _executeTrackedNext;
  }
}
