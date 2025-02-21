package arez;

import arez.spy.ComputableValueActivateEvent;
import arez.spy.ComputableValueDeactivateEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObservableValueDisposeEvent;
import arez.spy.ObservableValueInfo;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import grim.annotations.OmitSymbol;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The observable represents state that can be observed within the system.
 */
public final class ObservableValue<T>
  extends Node
{
  /**
   * The value of _workState when the ObservableValue is should longer be used.
   */
  static final int DISPOSED = -2;
  /**
   * The value that _workState is set to to optimize the detection of duplicate,
   * existing and new dependencies during tracking completion.
   */
  static final int IN_CURRENT_TRACKING = -1;
  /**
   * The value that _workState is when the observer has been added as new dependency
   * to derivation.
   */
  static final int NOT_IN_CURRENT_TRACKING = 0;
  private final List<Observer> _observers = new ArrayList<>();
  /**
   * True if deactivation has been requested.
   * Used to avoid adding duplicates to pending deactivation list.
   */
  private boolean _pendingDeactivation;
  /**
   * The workState variable contains some data used during processing of observable
   * at various stages.
   *
   * Within the scope of a tracking transaction, it is set to the id of the tracking
   * observer if the observable was observed. This enables an optimization that skips
   * adding this observer to the same observer multiple times. This optimization sometimes
   * ignored as nested transactions that observe the same observer will reset this value.
   *
   * When completing a tracking transaction the value may be set to {@link #IN_CURRENT_TRACKING}
   * or {@link #NOT_IN_CURRENT_TRACKING} but should be set to {@link #NOT_IN_CURRENT_TRACKING} after
   * {@link Transaction#completeTracking()} method is completed..
   */
  private int _workState;
  /**
   * The state of the observer that is least stale.
   * This cached value is used to avoid redundant propagations.
   */
  private int _leastStaleObserverState = Observer.Flags.STATE_UP_TO_DATE;
  /**
   * The observer that created this observable if any.
   */
  @Nullable
  private final Observer _observer;
  /**
   * The component that this observable is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the observable is a "top-level" observable.
   */
  @OmitSymbol( unless = "arez.enable_native_components" )
  @Nullable
  private final Component _component;
  /**
   * The accessor method to retrieve the value.
   * This should only be set if {@link Arez#arePropertyIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be accessed even by DevTools.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  @Nullable
  private final PropertyAccessor<T> _accessor;
  /**
   * The mutator method to change the value.
   * This should only be set if {@link Arez#arePropertyIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be mutated even by DevTools.
   */
  @OmitSymbol( unless = "arez.enable_property_introspection" )
  @Nullable
  private final PropertyMutator<T> _mutator;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nullable
  private ObservableValueInfo _info;

  ObservableValue( @Nullable final ArezContext context,
                   @Nullable final Component component,
                   @Nullable final String name,
                   @Nullable final Observer observer,
                   @Nullable final PropertyAccessor<T> accessor,
                   @Nullable final PropertyMutator<T> mutator )
  {
    super( context, name );
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _observer = observer;
    _accessor = accessor;
    _mutator = mutator;
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areNativeComponentsEnabled() || null == component,
                 () -> "Arez-0054: ObservableValue named '" + getName() + "' has component specified but " +
                       "Arez.areNativeComponentsEnabled() is false." );
    }
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.arePropertyIntrospectorsEnabled() || null == accessor,
                    () -> "Arez-0055: ObservableValue named '" + getName() + "' has accessor specified but " +
                          "Arez.arePropertyIntrospectorsEnabled() is false." );
      apiInvariant( () -> Arez.arePropertyIntrospectorsEnabled() || null == mutator,
                    () -> "Arez-0056: ObservableValue named '" + getName() + "' has mutator specified but " +
                          "Arez.arePropertyIntrospectorsEnabled() is false." );
    }
    if ( null != _observer )
    {
      // This invariant can not be checked if Arez.shouldEnforceTransactionType() is false as
      // the variable has yet to be assigned and no transaction mode set. Thus just skip the
      // check in this scenario.
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> !Arez.shouldEnforceTransactionType() || _observer.isComputableValue(),
                   () -> "Arez-0057: ObservableValue named '" + getName() + "' has observer specified but " +
                         "observer is not part of a ComputableValue." );
      }
      assert !Arez.areNamesEnabled() || _observer.getName().equals( name );
    }
    if ( !isComputableValue() )
    {
      if ( null != _component )
      {
        _component.addObservableValue( this );
      }
      else if ( Arez.areRegistriesEnabled() )
      {
        getContext().registerObservableValue( this );
      }
    }
  }

  @Override
  public void dispose()
  {
    if ( isNotDisposed() )
    {
      getContext().safeAction( Arez.areNamesEnabled() ? getName() + ".dispose" : null, this::performDispose );
      // All dependencies should have been released by the time it comes to deactivate phase.
      // The ObservableValue has been marked as changed, forcing all observers to re-evaluate and
      // ultimately this will result in their removal of this ObservableValue as a dependency as
      // it is an error to invoke reportObserved(). Once all dependencies are removed then
      // this ObservableValue will be deactivated if it is a ComputableValue. Thus no need to call
      // queueForDeactivation() here.
      if ( isComputableValue() )
      {
        /*
         * Dispose the owner first so that it is removed as a dependency and thus will not have a reaction
         * scheduled.
         */
        getObserver().dispose();
      }
      else
      {
        if ( willPropagateSpyEvents() )
        {
          reportSpyEvent( new ObservableValueDisposeEvent( asInfo() ) );
        }
        if ( null != _component )
        {
          _component.removeObservableValue( this );
        }
        else if ( Arez.areRegistriesEnabled() )
        {
          getContext().deregisterObservableValue( this );
        }
      }
    }
  }

  private void performDispose()
  {
    reportChanged();
    getContext().getTransaction().reportDispose( this );
    _workState = DISPOSED;
  }

  @Override
  public boolean isDisposed()
  {
    return DISPOSED == _workState;
  }

  @OmitSymbol( unless = "arez.enable_property_introspection" )
  @Nullable
  PropertyAccessor<T> getAccessor()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0058: Attempt to invoke getAccessor() on ObservableValue named '" + getName() +
                       "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    return _accessor;
  }

  @OmitSymbol( unless = "arez.enable_property_introspection" )
  @Nullable
  PropertyMutator<T> getMutator()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::arePropertyIntrospectorsEnabled,
                 () -> "Arez-0059: Attempt to invoke getMutator() on ObservableValue named '" + getName() +
                       "' when Arez.arePropertyIntrospectorsEnabled() returns false." );
    }
    return _mutator;
  }

  void markAsPendingDeactivation()
  {
    _pendingDeactivation = true;
  }

  boolean isPendingDeactivation()
  {
    return _pendingDeactivation;
  }

  void resetPendingDeactivation()
  {
    _pendingDeactivation = false;
  }

  int getLastTrackerTransactionId()
  {
    return _workState;
  }

  void setLastTrackerTransactionId( final int lastTrackerTransactionId )
  {
    setWorkState( lastTrackerTransactionId );
  }

  void setWorkState( final int workState )
  {
    _workState = workState;
  }

  boolean isInCurrentTracking()
  {
    return IN_CURRENT_TRACKING == _workState;
  }

  void putInCurrentTracking()
  {
    _workState = IN_CURRENT_TRACKING;
  }

  void removeFromCurrentTracking()
  {
    _workState = NOT_IN_CURRENT_TRACKING;
  }

  @Nonnull
  Observer getObserver()
  {
    assert null != _observer;
    return _observer;
  }

  /**
   * Return true if this observable can deactivate when it is no longer observed and has no keepAlive locks and activate when it is observed again.
   */
  boolean canDeactivate()
  {
    return isComputableValue() && !getObserver().isKeepAlive();
  }

  boolean canDeactivateNow()
  {
    return canDeactivate() && !hasObservers() && 0 == getObserver().getComputableValue().getKeepAliveRefCount();
  }

  /**
   * Return true if this observable is derived from an observer.
   */
  boolean isComputableValue()
  {
    return null != _observer;
  }

  /**
   * Return true if observable is notifying observers.
   */
  boolean isActive()
  {
    return null == _observer || _observer.isActive();
  }

  /**
   * Deactivate the observable.
   * This means that the observable no longer has any listeners and can release resources associated
   * with generating values. (i.e. remove observers on any observables that are used to compute the
   * value of this observable).
   */
  void deactivate()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0060: Attempt to invoke deactivate on ObservableValue named '" + getName() +
                       "' when there is no active transaction." );
      invariant( this::canDeactivate,
                 () -> "Arez-0061: Invoked deactivate on ObservableValue named '" + getName() + "' but " +
                       "ObservableValue can not be deactivated. Either owner is null or the associated " +
                       "ComputableValue has keepAlive enabled." );
    }
    assert null != _observer;
    if ( _observer.isActive() )
    {
      // We do not need to send deactivate even if the computable value was accessed from within an action
      // and has no associated observers. There has been no associated "Activate" event so there need not
      // be a deactivate event.
      final boolean shouldPropagateDeactivateEvent = willPropagateSpyEvents() && !getObservers().isEmpty();

      /*
       * It is possible for the owner to already be deactivated if dispose() is explicitly
       * called within the transaction.
       */
      _observer.setState( Observer.Flags.STATE_INACTIVE );
      if ( willPropagateSpyEvents() && shouldPropagateDeactivateEvent )
      {
        reportSpyEvent( new ComputableValueDeactivateEvent( _observer.getComputableValue().asInfo() ) );
      }
    }
  }

  /**
   * Activate the observable.
   * The reverse of {@link #deactivate()}.
   */
  void activate()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0062: Attempt to invoke activate on ObservableValue named '" + getName() +
                       "' when there is no active transaction." );
      invariant( () -> null != _observer,
                 () -> "Arez-0063: Invoked activate on ObservableValue named '" + getName() + "' when owner is null." );
      assert null != _observer;
      invariant( _observer::isInactive,
                 () -> "Arez-0064: Invoked activate on ObservableValue named '" + getName() + "' when " +
                       "ObservableValue is already active." );
    }
    assert null != _observer;
    _observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ComputableValueActivateEvent( _observer.getComputableValue().asInfo() ) );
    }
  }

  @Nonnull
  List<Observer> getObservers()
  {
    return _observers;
  }

  boolean hasObservers()
  {
    return !getObservers().isEmpty();
  }

  boolean hasObserver( @Nonnull final Observer observer )
  {
    return getObservers().contains( observer );
  }

  void addObserver( @Nonnull final Observer observer )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0065: Attempt to invoke addObserver on ObservableValue named '" + getName() +
                       "' when there is no active transaction." );
      invariantObserversLinked();
      invariant( () -> !hasObserver( observer ),
                 () -> "Arez-0066: Attempting to add observer named '" + observer.getName() + "' to ObservableValue " +
                       "named '" + getName() + "' when observer is already observing ObservableValue." );
      invariant( this::isNotDisposed,
                 () -> "Arez-0067: Attempting to add observer named '" + observer.getName() + "' to " +
                       "ObservableValue named '" + getName() + "' when ObservableValue is disposed." );
      invariant( observer::isNotDisposed,
                 () -> "Arez-0068: Attempting to add observer named '" + observer.getName() + "' to ObservableValue " +
                       "named '" + getName() + "' when observer is disposed." );
      invariant( () -> !isComputableValue() ||
                       observer.canObserveLowerPriorityDependencies() ||
                       observer.getTask().getPriority().ordinal() >= getObserver().getTask().getPriority().ordinal(),
                 () -> "Arez-0183: Attempting to add observer named '" + observer.getName() + "' to ObservableValue " +
                       "named '" + getName() + "' where the observer is scheduled at a " +
                       observer.getTask().getPriority() + " priority but the ObservableValue's owner is scheduled " +
                       "at a " + getObserver().getTask().getPriority() + " priority." );
      invariant( () -> getContext().getTransaction().getTracker() == observer,
                 () -> "Arez-0203: Attempting to add observer named '" + observer.getName() + "' to ObservableValue " +
                       "named '" + getName() + "' but the observer is not the tracker in transaction named '" +
                       getContext().getTransaction().getName() + "'." );
    }
    rawAddObserver( observer );
  }

  void rawAddObserver( @Nonnull final Observer observer )
  {
    getObservers().add( observer );

    final int state = observer.getLeastStaleObserverState();
    if ( _leastStaleObserverState > state )
    {
      _leastStaleObserverState = state;
    }
  }

  void removeObserver( @Nonnull final Observer observer )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0069: Attempt to invoke removeObserver on ObservableValue named '" + getName() + "' " +
                       "when there is no active transaction." );
      invariantObserversLinked();
      invariant( () -> hasObserver( observer ),
                 () -> "Arez-0070: Attempting to remove observer named '" + observer.getName() + "' from " +
                       "ObservableValue named '" + getName() + "' when observer is not observing ObservableValue." );
    }
    final List<Observer> observers = getObservers();
    observers.remove( observer );
    if ( canDeactivateNow() )
    {
      queueForDeactivation();
    }
    if ( Arez.shouldCheckInvariants() )
    {
      invariantObserversLinked();
    }
  }

  void queueForDeactivation()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0071: Attempt to invoke queueForDeactivation on ObservableValue named '" + getName() +
                       "' when there is no active transaction." );
      invariant( this::canDeactivateNow,
                 () -> "Arez-0072: Attempted to invoke queueForDeactivation() on ObservableValue named '" + getName() +
                       "' but ObservableValue is not able to be deactivated." );
      invariant( () -> !hasObservers(),
                 () -> "Arez-0073: Attempted to invoke queueForDeactivation() on ObservableValue named '" + getName() +
                       "' but ObservableValue has observers." );
    }
    if ( !isPendingDeactivation() )
    {
      getContext().getTransaction().queueForDeactivation( this );
    }
  }

  void setLeastStaleObserverState( final int leastStaleObserverState )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0074: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                       getName() + "' when there is no active transaction." );
      invariant( () -> Observer.Flags.isActive( leastStaleObserverState ),
                 () -> "Arez-0075: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                       getName() + "' with invalid value " + Observer.Flags.getStateName( leastStaleObserverState ) +
                       "." );
    }
    _leastStaleObserverState = leastStaleObserverState;
  }

  int getLeastStaleObserverState()
  {
    return _leastStaleObserverState;
  }

  /**
   * Notify Arez that this observable has been "observed" in the current transaction.
   * Before invoking this method, a transaction <b>MUST</b> be active but it may be read-only or read-write.
   */
  public void reportObserved()
  {
    getContext().getTransaction().observe( this );
  }

  /**
   * Notify Arez that this observable has been "observed" if a tracking transaction is active.
   */
  public void reportObservedIfTrackingTransactionActive()
  {
    if ( getContext().isTrackingTransactionActive() )
    {
      reportObserved();
    }
  }

  /**
   * Check that pre-conditions are satisfied before changing observable value.
   * In production mode this will typically be a no-op. This method should be invoked
   * before state is modified. Before invoking this method, a read-write transaction <b>MUST</b> be active.
   */
  @OmitSymbol( unless = "arez.check_invariants" )
  public void preReportChanged()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      getContext().getTransaction().preReportChanged( this );
    }
  }

  /**
   * Notify Arez that this observable has changed.
   * This is called when the observable has definitely changed.
   * Before invoking this method, a read-write transaction <b>MUST</b> be active.
   */
  public void reportChanged()
  {
    if ( willPropagateSpyEvents() )
    {
      // isDisposed is checked as we call reportChanged() from performDispose() after dispose has started
      // and thus it is no longer valid to call getObservableValue()
      reportSpyEvent( new ObservableValueChangeEvent( asInfo(), isDisposed() ? null : getObservableValue() ) );
    }
    getContext().getTransaction().reportChanged( this );
  }

  void reportChangeConfirmed()
  {
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableValueChangeEvent( asInfo(), getObservableValue() ) );
    }
    getContext().getTransaction().reportChangeConfirmed( this );
  }

  void reportPossiblyChanged()
  {
    getContext().getTransaction().reportPossiblyChanged( this );
  }

  /**
   * Return the value from observable if introspectors are enabled and an accessor has been supplied.
   */
  @Nullable
  private Object getObservableValue()
  {
    if ( Arez.arePropertyIntrospectorsEnabled() && null != getAccessor() )
    {
      try
      {
        return getAccessor().get();
      }
      catch ( final Throwable ignored )
      {
      }
    }
    return null;
  }

  /**
   * Return the info associated with this class.
   *
   * @return the info associated with this class.
   */
  @SuppressWarnings( "ConstantConditions" )
  @OmitSymbol( unless = "arez.enable_spies" )
  @Nonnull
  ObservableValueInfo asInfo()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( Arez::areSpiesEnabled,
                 () -> "Arez-0196: ObservableValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
    }
    if ( Arez.areSpiesEnabled() && null == _info )
    {
      _info = new ObservableValueInfoImpl( this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  void invariantOwner()
  {
    if ( Arez.shouldCheckInvariants() && null != _observer )
    {
      invariant( () -> Objects.equals( _observer.getComputableValue().getObservableValue(), this ),
                 () -> "Arez-0076: ObservableValue named '" + getName() + "' has owner specified but owner does " +
                       "not link to ObservableValue as derived value." );
    }
  }

  void invariantObserversLinked()
  {
    if ( Arez.shouldCheckExpensiveInvariants() )
    {
      getObservers().forEach( observer ->
                                invariant( () -> observer.getDependencies().contains( this ),
                                           () -> "Arez-0077: ObservableValue named '" + getName() + "' has observer " +
                                                 "named '" + observer.getName() + "' which does not contain " +
                                                 "ObservableValue as dependency." ) );
    }
  }

  void invariantLeastStaleObserverState()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      final int leastStaleObserverState =
        getObservers().stream().
          map( Observer::getLeastStaleObserverState ).
          min( Comparator.naturalOrder() ).orElse( Observer.Flags.STATE_UP_TO_DATE );
      invariant( () -> leastStaleObserverState >= _leastStaleObserverState,
                 () -> "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" +
                       getName() + "' is '" + Observer.Flags.getStateName( leastStaleObserverState ) +
                       "' which is unexpectedly less than cached value '" +
                       Observer.Flags.getStateName( _leastStaleObserverState ) + "'." );
    }
  }

  @Nullable
  Component getComponent()
  {
    return _component;
  }

  @OmitSymbol
  int getWorkState()
  {
    return _workState;
  }
}
