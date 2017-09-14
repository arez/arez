package org.realityforge.arez;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.spy.ComputedValueActivatedEvent;
import org.realityforge.arez.spy.ComputedValueDeactivatedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.ObservableDisposedEvent;

/**
 * The observable represents state that can be observed within the system.
 */
public final class Observable
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

  Observable( @Nonnull final ArezContext context, @Nullable final String name )
  {
    this( context, name, null );
  }

  Observable( @Nonnull final ArezContext context, @Nullable final String name, @Nullable final Observer owner )
  {
    super( context, name );
    _owner = owner;
    if ( null != _owner )
    {
      Guards.invariant( _owner::isDerivation,
                        () -> String.format( "Observable named '%s' has owner specified but owner is not a derivation.",
                                             getName() ) );
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
        getContext().safeProcedure( ArezConfig.enableNames() ? getName() : null,
                                    TransactionMode.READ_WRITE,
                                    null,
                                    this::performDispose );
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
    if ( willPropagateSpyEvents() && !isCalculated() )
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

  @Nullable
  Observer getOwner()
  {
    return _owner;
  }

  /**
   * Return true if this observable can deactivate when it is no longer observable and activate when it is observable again.
   */
  boolean canDeactivate()
  {
    return isCalculated();
  }

  /**
   * Return true if this observable is derived from an observer.
   */
  boolean isCalculated()
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
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format( "Attempt to invoke deactivate on observable named '%s' when there is " +
                                           "no active transaction.", getName() ) );
    Guards.invariant( () -> null != _owner,
                      () -> String.format( "Invoked deactivate on observable named '%s' when owner is null.",
                                           getName() ) );
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
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke activate on observable named '%s' when there is no active transaction.",
                        getName() ) );
    Guards.invariant( () -> null != _owner,
                      () -> String.format( "Invoked activate on observable named '%s' when owner is null.",
                                           getName() ) );
    assert null != _owner;
    Guards.invariant( _owner::isInactive,
                      () -> String.format(
                        "Invoked activate on observable named '%s' when observable is already active.",
                        getName() ) );
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
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke addObserver on observable named '%s' when there is no active transaction.",
                        getName() ) );
    invariantObserversLinked();
    Guards.invariant( () -> !hasObserver( observer ),
                      () -> String.format(
                        "Attempting to add observer named '%s' to observable named '%s' when observer is already observing observable.",
                        observer.getName(),
                        getName() ) );
    Guards.invariant( () -> !isDisposed(),
                      () -> String.format( "Attempting to add observer named '%s' to observable named '%s' when " +
                                           "observable is disposed.", observer.getName(), getName() ) );
    Guards.invariant( () -> !observer.isDisposed(),
                      () -> String.format( "Attempting to add observer named '%s' to observable named '%s' when " +
                                           "observer is disposed.", observer.getName(), getName() ) );
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
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke removeObserver on observable named '%s' when there is no active transaction.",
                        getName() ) );
    invariantObserversLinked();
    Guards.invariant( () -> hasObserver( observer ),
                      () -> String.format(
                        "Attempting to remove observer named '%s' from observable named '%s' when observer is already observing observable.",
                        observer.getName(),
                        getName() ) );
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
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke queueForDeactivation on observable named '%s' when there is no active transaction.",
                        getName() ) );
    Guards.invariant( this::canDeactivate,
                      () -> String.format(
                        "Attempted to invoke queueForDeactivation() on observable named '%s' but observable is not able to be deactivated.",
                        getName() ) );
    Guards.invariant( () -> !hasObservers(),
                      () -> String.format(
                        "Attempted to invoke queueForDeactivation() on observable named '%s' but observable has observers.",
                        getName() ) );
    if ( !_pendingDeactivation )
    {
      _pendingDeactivation = true;
      getContext().getTransaction().queueForDeactivation( this );
    }
  }

  void setLeastStaleObserverState( @Nonnull final ObserverState leastStaleObserverState )
  {
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempt to invoke setLeastStaleObserverState on observable named '%s' when there is no active transaction.",
                        getName() ) );
    Guards.invariant( () -> ObserverState.INACTIVE != leastStaleObserverState,
                      () -> String.format(
                        "Attempt to invoke setLeastStaleObserverState on observable named '%s' with invalid value INACTIVE.",
                        getName() ) );
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
      Guards.invariant( () -> Objects.equals( _owner.getDerivedValue(), this ),
                        () -> String.format( "Observable named '%s' has owner specified but owner does not link to " +
                                             "observable as derived value.", getName() ) );
    }
  }

  void invariantObserversLinked()
  {
    getObservers().forEach( observer ->
                              Guards.invariant( () -> observer.getDependencies().contains( this ),
                                                () -> String.format(
                                                  "Observable named '%s' has observer named '%s' which does not contain observable as dependency.",
                                                  getName(),
                                                  observer.getName() ) ) );
  }

  void invariantLeastStaleObserverState()
  {
    final ObserverState leastStaleObserverState =
      getObservers().stream().
        map( Observer::getState ).map( s -> s == ObserverState.INACTIVE ? ObserverState.UP_TO_DATE : s ).
        min( Comparator.comparing( Enum::ordinal ) ).orElse( ObserverState.UP_TO_DATE );
    Guards.invariant( () -> leastStaleObserverState.ordinal() >= _leastStaleObserverState.ordinal(),
                      () -> String.format(
                        "Calculated leastStaleObserverState on observable named '%s' is '%s' which is unexpectedly less than cached value '%s'.",
                        getName(),
                        leastStaleObserverState.name(),
                        _leastStaleObserverState.name() ) );
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
