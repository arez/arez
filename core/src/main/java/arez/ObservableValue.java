package arez;

import arez.spy.ComputedValueActivatedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.ObservableValueDisposedEvent;
import arez.spy.ObservableValueInfo;
import arez.spy.PropertyAccessor;
import arez.spy.PropertyMutator;
import java.util.ArrayList;
import java.util.Comparator;
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
  private final ArrayList<Observer> _observers = new ArrayList<>();
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
  @Nonnull
  private ObserverState _leastStaleObserverState = ObserverState.UP_TO_DATE;
  /**
   * The observer that created this observable if any.
   */
  @Nullable
  private final Observer _owner;
  /**
   * The component that this observable is contained within.
   * This should only be set if {@link Arez#areNativeComponentsEnabled()} is true but may also be null if
   * the observable is a "top-level" observable.
   */
  @Nullable
  private final Component _component;
  /**
   * The accessor method to retrieve the value.
   * This should only be set if {@link Arez#arePropertyIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be accessed even by DevTools.
   */
  @Nullable
  private final PropertyAccessor<T> _accessor;
  /**
   * The mutator method to change the value.
   * This should only be set if {@link Arez#arePropertyIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be mutated even by DevTools.
   */
  @Nullable
  private final PropertyMutator<T> _mutator;
  /**
   * Cached info object associated with element.
   * This should be null if {@link Arez#areSpiesEnabled()} is false;
   */
  @Nullable
  private ObservableValueInfo _info;

  ObservableValue( @Nullable final ArezContext context,
                   @Nullable final Component component,
                   @Nullable final String name,
                   @Nullable final Observer owner,
                   @Nullable final PropertyAccessor<T> accessor,
                   @Nullable final PropertyMutator<T> mutator )
  {
    super( context, name );
    _component = Arez.areNativeComponentsEnabled() ? component : null;
    _owner = owner;
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
    if ( null != _owner )
    {
      // This invariant can not be checked if Arez.shouldEnforceTransactionType() is false as
      // the variable has yet to be assigned and no transaction mode set. Thus just skip the
      // check in this scenario.
      if ( Arez.shouldCheckInvariants() )
      {
        invariant( () -> !Arez.shouldEnforceTransactionType() || _owner.isComputedValue(),
                   () -> "Arez-0057: ObservableValue named '" + getName() + "' has owner specified " +
                         "but owner is not a derivation." );
      }
      assert !Arez.areNamesEnabled() || _owner.getName().equals( name );
    }
    if ( !hasOwner() )
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

  /**
   * {@inheritDoc}
   */
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
      // this ObservableValue will be deactivated if it is a ComputedValue. Thus no need to call
      // queueForDeactivation() here.
      if ( hasOwner() )
      {
        /*
         * Dispose the owner first so that it is removed as a dependency and thus will not have a reaction
         * scheduled.
         */
        getOwner().dispose();
      }
      else
      {
        if ( willPropagateSpyEvents() )
        {
          reportSpyEvent( new ObservableValueDisposedEvent( asInfo() ) );
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
    getContext().getTransaction().reportDispose( this );
    reportChanged();
    _workState = DISPOSED;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return DISPOSED == _workState;
  }

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
  Observer getOwner()
  {
    assert null != _owner;
    return _owner;
  }

  /**
   * Return true if this observable can deactivate when it is no longer observable and activate when it is observable again.
   */
  boolean canDeactivate()
  {
    return hasOwner() && !getOwner().getComputedValue().isKeepAlive();
  }

  /**
   * Return true if this observable is derived from an observer.
   */
  boolean hasOwner()
  {
    return null != _owner;
  }

  /**
   * Return true if observable is notifying observers.
   */
  boolean isActive()
  {
    return null == _owner || _owner.isActive();
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
                       "ComputedValue has keepAlive enabled." );
    }
    assert null != _owner;
    if ( _owner.isActive() )
    {
      /*
       * It is possible for the owner to already be deactivated if dispose() is explicitly
       * called within the transaction.
       */
      _owner.setState( ObserverState.INACTIVE );
      if ( willPropagateSpyEvents() )
      {
        reportSpyEvent( new ComputedValueDeactivatedEvent( _owner.getComputedValue().asInfo() ) );
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
      invariant( () -> null != _owner,
                 () -> "Arez-0063: Invoked activate on ObservableValue named '" + getName() + "' when owner is null." );
      assert null != _owner;
      invariant( _owner::isInactive,
                 () -> "Arez-0064: Invoked activate on ObservableValue named '" + getName() + "' when " +
                       "ObservableValue is already active." );
    }
    assert null != _owner;
    _owner.setState( ObserverState.UP_TO_DATE );
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ComputedValueActivatedEvent( _owner.getComputedValue().asInfo() ) );
    }
  }

  @Nonnull
  ArrayList<Observer> getObservers()
  {
    return _observers;
  }

  boolean hasObservers()
  {
    return getObservers().size() > 0;
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
      invariant( () -> !hasOwner() ||
                       observer.canObserveLowerPriorityDependencies() ||
                       observer.getPriority().ordinal() >= getOwner().getPriority().ordinal(),
                 () -> "Arez-0183: Attempting to add observer named '" + observer.getName() + "' to ObservableValue " +
                       "named '" + getName() + "' where the observer is scheduled at a " + observer.getPriority() +
                       " priority but the ObservableValue's owner is scheduled at a " +
                       getOwner().getPriority() + " priority." );
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

    final ObserverState state = ObserverState.getLeastStaleObserverState( observer.getState() );
    if ( _leastStaleObserverState.ordinal() > state.ordinal() )
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
    final ArrayList<Observer> observers = getObservers();
    observers.remove( observer );
    if ( observers.isEmpty() && canDeactivate() )
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
      invariant( this::canDeactivate,
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

  void setLeastStaleObserverState( @Nonnull final ObserverState leastStaleObserverState )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> getContext().isTransactionActive(),
                 () -> "Arez-0074: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                       getName() + "' when there is no active transaction." );
      invariant( () -> ObserverState.isActive( leastStaleObserverState ),
                 () -> "Arez-0075: Attempt to invoke setLeastStaleObserverState on ObservableValue named '" +
                       getName() + "' with invalid value " + leastStaleObserverState + "." );
    }
    _leastStaleObserverState = leastStaleObserverState;
  }

  @Nonnull
  final ObserverState getLeastStaleObserverState()
  {
    return _leastStaleObserverState;
  }

  /**
   * Notify Arez that this observable has been "observed" in the current transaction.
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
   * before state is modified.
   */
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
   */
  public void reportChanged()
  {
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableValueChangedEvent( asInfo(), getObservableValue() ) );
    }
    getContext().getTransaction().reportChanged( this );
  }

  void reportChangeConfirmed()
  {
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableValueChangedEvent( asInfo(), getObservableValue() ) );
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
      _info = new ObservableValueInfoImpl( getContext().getSpy(), this );
    }
    return Arez.areSpiesEnabled() ? _info : null;
  }

  void invariantOwner()
  {
    if ( Arez.shouldCheckInvariants() && null != _owner )
    {
      invariant( () -> Objects.equals( _owner.getComputedValue().getObservableValue(), this ),
                 () -> "Arez-0076: ObservableValue named '" + getName() + "' has owner specified but owner does " +
                       "not link to ObservableValue as derived value." );
    }
  }

  void invariantObserversLinked()
  {
    if ( Arez.shouldCheckInvariants() )
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
      final ObserverState leastStaleObserverState =
        getObservers().stream().
          map( Observer::getState ).map( s -> ObserverState.isNotActive( s ) ? ObserverState.UP_TO_DATE : s ).
          min( Comparator.comparing( Enum::ordinal ) ).orElse( ObserverState.UP_TO_DATE );
      invariant( () -> leastStaleObserverState.ordinal() >= _leastStaleObserverState.ordinal(),
                 () -> "Arez-0078: Calculated leastStaleObserverState on ObservableValue named '" + getName() +
                       "' is '" + leastStaleObserverState.name() + "' which is unexpectedly less " +
                       "than cached value '" + _leastStaleObserverState.name() + "'." );
    }
  }

  @Nullable
  Component getComponent()
  {
    return _component;
  }

  int getWorkState()
  {
    return _workState;
  }
}
