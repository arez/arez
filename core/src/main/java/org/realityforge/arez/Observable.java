package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;
import org.realityforge.arez.spy.ComputedValueActivatedEvent;
import org.realityforge.arez.spy.ComputedValueDeactivatedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.ObservableDisposedEvent;
import org.realityforge.arez.spy.PropertyAccessor;
import org.realityforge.arez.spy.PropertyMutator;
import static org.realityforge.braincheck.Guards.*;

/**
 * The observable represents state that can be observed within the system.
 */
public final class Observable<T>
  extends Node
{
  /**
   * The value of _workState when the Observable is should longer be used.
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
   * The accessor method to retrieve the value.
   * This should only be set if {@link Arez#areValueIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be accessed even by DevTools.
   */
  @Nullable
  private final PropertyAccessor<T> _accessor;
  /**
   * The mutator method to change the value.
   * This should only be set if {@link Arez#areValueIntrospectorsEnabled()} is true but may also be elided if the
   * value should not be mutated even by DevTools.
   */
  @Nullable
  private final PropertyMutator<T> _mutator;

  Observable( @Nonnull final ArezContext context,
              @Nullable final String name,
              @Nullable final Observer owner,
              @Nullable final PropertyAccessor<T> accessor,
              @Nullable final PropertyMutator<T> mutator )
  {
    super( context, name );
    _owner = owner;
    _accessor = accessor;
    _mutator = mutator;
    apiInvariant( () -> Arez.areValueIntrospectorsEnabled() || null == accessor,
                  () -> "Observable named '" + getName() + "' has accessor specified but Arez.areValueIntrospectorsEnabled() is false." );
    apiInvariant( () -> Arez.areValueIntrospectorsEnabled() || null == mutator,
                  () -> "Observable named '" + getName() + "' has mutator specified but Arez.areValueIntrospectorsEnabled() is false." );
    if ( null != _owner )
    {
      // This invariant can not be checked if ArezConfig.enforceTransactionType() is false as
      // the variable has yet to be assigned and no transaction mode set. Thus just skip the
      // check in this scenario.
      invariant( () -> !ArezConfig.enforceTransactionType() || _owner.isDerivation(),
                 () -> "Observable named '" + getName() + "' has owner specified " +
                       "but owner is not a derivation." );
      assert !Arez.areNamesEnabled() || _owner.getName().equals( name );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    if ( !isDisposed() )
    {
      if ( getContext().isTransactionActive() )
      {
        performDispose();
      }
      else
      {
        getContext().safeAction( Arez.areNamesEnabled() ? getName() : null,
                                 ArezConfig.enforceTransactionType() ? TransactionMode.READ_WRITE : null,
                                 this::performDispose,
                                 null );
      }
    }
  }

  private void performDispose()
  {
    reportChanged();
    _workState = DISPOSED;
    // All dependencies should have been released by the time it comes to deactivate phase.
    // The Observable has been marked as changed, forcing all observers to re-evaluate and
    // ultimately this will result in their removal of this Observable as a dependency as
    // it is an error to invoke reportObserved(). Once all dependencies are removed then
    // this Observable will be deactivated if it is a ComputedValue. Thus no need to call
    // queueForDeactivation() here.
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableDisposedEvent( this ) );
    }
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
    invariant( Arez::areValueIntrospectorsEnabled,
               () -> "Attempt to invoke getAccessor() on observable named '" + getName() +
                     "' when Arez.areValueIntrospectorsEnabled() returns false." );
    return _accessor;
  }

  @Nullable
  PropertyMutator<T> getMutator()
  {
    invariant( Arez::areValueIntrospectorsEnabled,
               () -> "Attempt to invoke getMutator() on observable named '" + getName() +
                     "' when Arez.areValueIntrospectorsEnabled() returns false." );
    return _mutator;
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
    return hasOwner();
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
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke deactivate on observable named '" + getName() +
                     "' when there is no active transaction." );
    invariant( () -> null != _owner,
               () -> "Invoked deactivate on observable named '" + getName() + "' when owner is null." );
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
        reportSpyEvent( new ComputedValueDeactivatedEvent( _owner.getComputedValue() ) );
      }
    }
  }

  /**
   * Activate the observable.
   * The reverse of {@link #deactivate()}.
   */
  void activate()
  {
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke activate on observable named '" + getName() + "' when there " +
                     "is no active transaction." );
    invariant( () -> null != _owner,
               () -> "Invoked activate on observable named '" + getName() + "' when owner is null." );
    assert null != _owner;
    invariant( _owner::isInactive,
               () -> "Invoked activate on observable named '" + getName() + "' when " +
                     "observable is already active." );
    _owner.setState( ObserverState.UP_TO_DATE );
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ComputedValueActivatedEvent( _owner.getComputedValue() ) );
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
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke addObserver on observable named '" + getName() +
                     "' when there is no active transaction." );
    invariantObserversLinked();
    invariant( () -> !hasObserver( observer ),
               () -> "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                     getName() + "' when observer is already observing observable." );
    invariant( () -> !isDisposed(),
               () -> "Attempting to add observer named '" + observer.getName() + "' to observable named '" +
                     getName() + "' when observable is disposed." );
    invariant( observer::isLive,
               () -> "Attempting to add observer named '" + observer.getName() + "' to observable " +
                     "named '" + getName() + "' when observer is disposed." );
    getObservers().add( observer );

    final ObserverState state =
      ObserverState.INACTIVE == observer.getState() ? ObserverState.UP_TO_DATE : observer.getState();
    if ( _leastStaleObserverState.ordinal() > state.ordinal() )
    {
      _leastStaleObserverState = state;
    }
  }

  void removeObserver( @Nonnull final Observer observer )
  {
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke removeObserver on observable named '" + getName() + "' " +
                     "when there is no active transaction." );
    invariantObserversLinked();
    invariant( () -> hasObserver( observer ),
               () -> "Attempting to remove observer named '" + observer.getName() + "' from observable " +
                     "named '" + getName() + "' when observer is already observing observable." );
    final ArrayList<Observer> observers = getObservers();
    observers.remove( observer );
    if ( observers.isEmpty() && canDeactivate() )
    {
      queueForDeactivation();
    }
    invariantObserversLinked();
  }

  void queueForDeactivation()
  {
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke queueForDeactivation on observable named '" + getName() +
                     "' when there is no active transaction." );
    invariant( this::canDeactivate,
               () -> "Attempted to invoke queueForDeactivation() on observable named '" + getName() +
                     "' but observable is not able to be deactivated." );
    invariant( () -> !hasObservers(),
               () -> "Attempted to invoke queueForDeactivation() on observable named '" + getName() +
                     "' but observable has observers." );
    if ( !_pendingDeactivation )
    {
      _pendingDeactivation = true;
      getContext().getTransaction().queueForDeactivation( this );
    }
  }

  void setLeastStaleObserverState( @Nonnull final ObserverState leastStaleObserverState )
  {
    invariant( () -> getContext().isTransactionActive(),
               () -> "Attempt to invoke setLeastStaleObserverState on observable named '" + getName() +
                     "' when there is no active transaction." );
    invariant( () -> ObserverState.INACTIVE != leastStaleObserverState,
               () -> "Attempt to invoke setLeastStaleObserverState on observable named '" + getName() +
                     "' with invalid value INACTIVE." );
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
   * Notify Arez that this observable has changed.
   * This is called when the observable has definitely changed.
   */
  public void reportChanged()
  {
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableChangedEvent( this ) );
    }
    getContext().getTransaction().reportChanged( this );
  }

  void reportChangeConfirmed()
  {
    if ( willPropagateSpyEvents() )
    {
      reportSpyEvent( new ObservableChangedEvent( this ) );
    }
    getContext().getTransaction().reportChangeConfirmed( this );
  }

  void reportPossiblyChanged()
  {
    getContext().getTransaction().reportPossiblyChanged( this );
  }

  void invariantOwner()
  {
    if ( null != _owner )
    {
      invariant( () -> Objects.equals( _owner.getDerivedValue(), this ),
                 () -> "Observable named '" + getName() + "' has owner specified but owner does not link to " +
                       "observable as derived value." );
    }
  }

  void invariantObserversLinked()
  {
    getObservers().forEach( observer ->
                              invariant( () -> observer.getDependencies().contains( this ),
                                         () -> "Observable named '" + getName() + "' has observer named '" +
                                               observer.getName() + "' which does not contain observable " +
                                               "as dependency." ) );
  }

  void invariantLeastStaleObserverState()
  {
    final ObserverState leastStaleObserverState =
      getObservers().stream().
        map( Observer::getState ).map( s -> s == ObserverState.INACTIVE ? ObserverState.UP_TO_DATE : s ).
        min( Comparator.comparing( Enum::ordinal ) ).orElse( ObserverState.UP_TO_DATE );
    invariant( () -> leastStaleObserverState.ordinal() >= _leastStaleObserverState.ordinal(),
               () -> "Calculated leastStaleObserverState on observable named '" + getName() +
                     "' is '" + leastStaleObserverState.name() + "' which is unexpectedly less " +
                     "than cached value '" + _leastStaleObserverState.name() + "'." );
  }

  @TestOnly
  boolean isPendingDeactivation()
  {
    return _pendingDeactivation;
  }

  @TestOnly
  int getWorkState()
  {
    return _workState;
  }

  @TestOnly
  void markAsPendingDeactivation()
  {
    _pendingDeactivation = true;
  }
}
