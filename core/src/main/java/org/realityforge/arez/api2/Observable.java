package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;

public abstract class Observable
  extends Node
{
  /**
   * The value that _workState is set to to optimize the detection of duplicate,
   * existing and new dependencies during tracking completion.
   */
  private static final int IN_CURRENT_TRACKING = -1;
  /**
   * The value that _workState is when the observer has been added as new dependency
   * to derivation.
   */
  static final int NOT_IN_CURRENT_TRACKING = 0;

  private final ArrayList<Observer> _observers = new ArrayList<>();
  /**
   * True if passivation has been requested in transaction.
   * Used to avoid adding duplicates to passivation list.
   */
  private boolean _pendingPassivation;
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
  private ObserverState _leastStaleObserverState = ObserverState.INACTIVE;
  /**
   * The derivation that created this observable if any.
   */
  @Nullable
  private final Derivation _owner;

  Observable( @Nonnull final ArezContext context, @Nullable final String name )
  {
    this( context, name, null );
  }

  Observable( @Nonnull final ArezContext context, @Nullable final String name, @Nullable final Derivation owner )
  {
    super( context, name );
    _owner = owner;
  }

  final void resetPendingPassivation()
  {
    _pendingPassivation = false;
  }

  final void reportObserved()
  {
    getContext().getTransaction().observe( this );
  }

  final int getLastTrackerTransactionId()
  {
    return _workState;
  }

  final void setLastTrackerTransactionId( final int lastTrackerTransactionId )
  {
    _workState = lastTrackerTransactionId;
  }

  final boolean isInCurrentTracking()
  {
    return IN_CURRENT_TRACKING == _workState;
  }

  final void putInCurrentTracking()
  {
    _workState = IN_CURRENT_TRACKING;
  }

  final void removeFromCurrentTracking()
  {
    _workState = NOT_IN_CURRENT_TRACKING;
  }

  @Nullable
  final Derivation getOwner()
  {
    return _owner;
  }

  /**
   * Return true if this observable can passivate when it is no longer observable and activate when it is observable again.
   */
  final boolean canPassivate()
  {
    return null != _owner;
  }

  /**
   * Return true if observable is notifying observers.
   */
  final boolean isActive()
  {
    return null == _owner || _owner.isActive();
  }

  /**
   * Passivate the observable.
   * This means that the observable no longer has any listeners and can release resources associated
   * with generating values. (i.e. remove observers on any observables that are used to compute the
   * value of this observable).
   */
  protected void passivate()
  {
    Guards.invariant( () -> null != _owner,
                      () -> String.format( "Invoked passivate on observable named '%s' when owner is null.",
                                           getName() ) );
    assert null != _owner;
    Guards.invariant( _owner::isActive,
                      () -> String.format( "Invoked passivate on observable named '%s' when owner is inactive.",
                                           getName() ) );
    _owner.setState( ObserverState.INACTIVE );
  }

  /**
   * Activate the observable.
   * The reverse of {@link #passivate()}.
   */
  protected void activate()
  {
    Guards.invariant( () -> !isActive(),
                      () -> String.format(
                        "Invoked activate on observable named '%s' when observable is already active.",
                        getName() ) );
  }

  @Nonnull
  final ArrayList<Observer> getObservers()
  {
    return _observers;
  }

  final boolean hasObservers()
  {
    return getObservers().size() > 0;
  }

  final void addObserver( @Nonnull final Observer observer )
  {
    invariantObserversLinked();
    Guards.invariant( () -> !getObservers().contains( observer ),
                      () -> String.format(
                        "Attempting to add observer named '%s' to observable named '%s' when observer is already observing observable.",
                        observer.getName(),
                        getName() ) );

    if ( !getObservers().add( observer ) )
    {
      Guards.fail( () -> String.format( "Failed to add observer named '%s' to observable named '%s'.",
                                        observer.getName(),
                                        getName() ) );
    }

    final ObserverState state = observer.getState();
    if ( _leastStaleObserverState.ordinal() > state.ordinal() )
    {
      _leastStaleObserverState = state;
    }
  }

  final void setLeastStaleObserverState( @Nonnull final ObserverState leastStaleObserverState )
  {
    _leastStaleObserverState = leastStaleObserverState;
  }

  @Nonnull
  final ObserverState getLeastStaleObserverState()
  {
    return _leastStaleObserverState;
  }

  final void removeObserver( @Nonnull final Observer observer )
  {
    invariantObserversLinked();
    final ArrayList<Observer> observers = getObservers();
    if ( !observers.remove( observer ) )
    {
      Guards.fail( () -> String.format(
        "Attempted to remove observer named '%s' from observable named '%s' but observer is not an observers.",
        observer.getName(),
        getName() ) );
    }
    if ( observers.isEmpty() && canPassivate() )
    {
      queueForPassivation();
    }
    invariantObserversLinked();
  }

  private void queueForPassivation()
  {
    Guards.invariant( this::canPassivate,
                      () -> String.format(
                        "Attempted to invoke queueForPassivation() on observable named '%s' but observable is not able to be passivated.",
                        getName() ) );
    Guards.invariant( () -> getObservers().isEmpty(),
                      () -> String.format(
                        "Attempted to invoke queueForPassivation() on observable named '%s' but observable has observers.",
                        getName() ) );
    if ( !_pendingPassivation )
    {
      _pendingPassivation = true;
      getContext().getTransaction().queueForPassivation( this );
    }
  }

  // Called by Atom when its value changes
  final void reportChanged()
  {
    getContext().getTransaction().reportChanged( this );
  }

  // Called by ComputedValue when it recalculate and its value changed
  final void reportChangeConfirmed()
  {
    getContext().getTransaction().reportChangeConfirmed( this );
  }

  // Used by computed when its dependency changed, but we don't wan't to immediately recompute.
  final void reportPossiblyChanged()
  {
    getContext().getTransaction().reportPossiblyChanged( this );
  }

  final void invariantObserversLinked()
  {
    getObservers().forEach( observer ->
                              Guards.invariant( () -> observer.getDependencies().contains( this ),
                                                () -> String.format(
                                                  "Observable named '%s' has observer named '%s' which does not contain observerable as dependency.",
                                                  getName(),
                                                  observer.getName() ) ) );
  }

  final void invariantLeastStaleObserverState()
  {
    final ObserverState leastStaleObserverState =
      getObservers().stream().
        map( Observer::getState ).min( Comparator.comparing( Enum::ordinal ) ).orElse( ObserverState.INACTIVE );
    Guards.invariant( () -> leastStaleObserverState.ordinal() >= _leastStaleObserverState.ordinal(),
                      () -> String.format(
                        "Calculated leastStaleObserverState on observable named '%s' is '%s' which is unexpectedly less than cached value '%s'.",
                        getName(),
                        leastStaleObserverState.name(),
                        _leastStaleObserverState.name() ) );
  }

  @TestOnly
  final boolean isPendingPassivation()
  {
    return _pendingPassivation;
  }

  @TestOnly
  final int getWorkState()
  {
    return _workState;
  }

  @TestOnly
  final void setPendingPassivation( final boolean pendingPassivation )
  {
    _pendingPassivation = pendingPassivation;
  }
}
