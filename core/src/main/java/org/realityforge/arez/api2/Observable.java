package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Comparator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Observable
  extends Node
{
  /**
   * The value that _lastTrackerTransactionId is set to to optimize the detection of duplicate,
   * existing and new dependencies during tracking completion.
   */
  private static final int IN_CURRENT_TRACKING = -1;
  /**
   * The value that _lastTrackerTransactionId is when the observer has been added as new dependency
   * to derivation.
   */
  private static final int NOT_IN_CURRENT_TRACKING = 0;

  private final ArrayList<Observer> _observers = new ArrayList<>();
  /**
   * True if passivation has been requested in transaction.
   * Used to avoid adding duplicates to passivation list.
   */
  private boolean _pendingPassivation;
  /**
   * The id of the tracking that last observed the observable.
   * This enables an optimization that skips adding this observer
   * to the same tracking multiple times.
   *
   * The value may also be set to {@link #IN_CURRENT_TRACKING} during the completion
   * of tracking operation.
   */
  private int _lastTrackerTransactionId;
  /**
   * The state of the observer that is least stale.
   * This cached value is used to avoid redundant propagations.
   */
  @Nonnull
  private ObserverState _leastStaleObserverState = ObserverState.NOT_TRACKING;
  /**
   * The derivation from which this observable is derived if any.
   */
  @Nullable
  private final Observer _observer;

  Observable( @Nonnull final ArezContext context, @Nullable final String name, @Nullable final Observer observer )
  {
    super( context, name );
    _observer = observer;
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
    return _lastTrackerTransactionId;
  }

  final void setLastTrackerTransactionId( final int lastTrackerTransactionId )
  {
    _lastTrackerTransactionId = lastTrackerTransactionId;
  }

  final boolean isInCurrentTracking()
  {
    return IN_CURRENT_TRACKING == _lastTrackerTransactionId;
  }

  final void putInCurrentTracking()
  {
    _lastTrackerTransactionId = IN_CURRENT_TRACKING;
  }

  final void removeFromCurrentTracking()
  {
    _lastTrackerTransactionId = NOT_IN_CURRENT_TRACKING;
  }

  @Nullable
  final Observer getObserver()
  {
    return _observer;
  }

  /**
   * Return true if this observable can passivate when it is no longer observable and activate when it is observable again.
   */
  final boolean canPassivate()
  {
    return null != _observer;
  }

  /**
   * Return true if observable is active and notifying observers.
   */
  private boolean isActive()
  {
    //return null == _observer || ObserverState.NOT_TRACKING != _observer.getState();
    return true;
  }

  /**
   * Passivate the observable.
   * This means that the observable no longer has any listeners and can release resources associated
   * with generating values. (i.e. remove observers on any observables that are used to compute the
   * value of this observable).
   */
  protected void passivate()
  {
    Guards.invariant( this::isActive,
                      () -> String.format( "Invoked passivate on observable named '%s' when observable is not active.",
                                           getName() ) );
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
    Guards.invariant( () -> observer.getState() == ObserverState.NOT_TRACKING,
                      () -> String.format(
                        "Attempting to add observer named '%s' to observable named '%s' when observer is in state '%s' rather than the expected 'NOT_TRACKING'.",
                        observer.getName(),
                        getName(),
                        observer.getState().name() ) );
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
      // In theory this code should never be executed.
      // In future it should be removed.
      Guards.fail( () -> String.format(
        "Attempting to update _leastStaleObserverState to '%s' from '%s' when adding observer named '%s' to observable named '%s'.",
        state.name(),
        _leastStaleObserverState.name(),
        observer.getName(),
        getName() ) );
      _leastStaleObserverState = state;
    }
  }

  final void removeObserver( @Nonnull final Observer observer )
  {
    Guards.invariant( () -> getContext().isTransactionActive(),
                      () -> String.format(
                        "Attempted to remove observer named '%s' from observable named '%s' when not in transaction.",
                        observer.getName(),
                        getName() ) );

    final ArrayList<Observer> observers = getObservers();
    if ( !observers.remove( observer ) )
    {
      Guards.fail( () -> String.format(
        "Attempted to remove observer named '%s' from observable named '%s' when not in batch.",
        observer.getName(),
        getName() ) );
    }
    if ( observers.isEmpty() && canPassivate() )
    {
      queueForPassivation();
    }
  }

  private void queueForPassivation()
  {
    if ( !_pendingPassivation )
    {
      _pendingPassivation = true;
      getContext().getTransaction().queueForPassivation( this );
    }
  }

  // Called by Atom when its value changes
  final void propagateChanged()
  {
    invariantLeastStaleObserverState();
    if ( ObserverState.STALE != _leastStaleObserverState )
    {
      _leastStaleObserverState = ObserverState.STALE;
      for ( final Observer observer : getObservers() )
      {
        final ObserverState state = observer.getState();
        if ( ObserverState.UP_TO_DATE == state )
        {
          observer.setState( ObserverState.STALE );
        }
      }
    }
    invariantLeastStaleObserverState();
  }

  // Called by ComputedValue when it recalculate and its value changed
  final void propagateChangeConfirmed()
  {
    invariantLeastStaleObserverState();
    if ( ObserverState.STALE != _leastStaleObserverState )
    {
      _leastStaleObserverState = ObserverState.STALE;

      for ( final Observer observer : getObservers() )
      {
        if ( ObserverState.POSSIBLY_STALE == observer.getState() )
        {
          observer.setState( ObserverState.STALE );
        }
        else if ( ObserverState.UP_TO_DATE == observer.getState() )
        {
          // this happens during computing of `observer`, just keep _leastStaleObserverState up to date.
          _leastStaleObserverState = ObserverState.UP_TO_DATE;
        }
      }
    }
    invariantLeastStaleObserverState();
  }

  // Used by computed when its dependency changed, but we don't wan't to immediately recompute.
  final void propagateMaybeChanged()
  {
    invariantLeastStaleObserverState();
    if ( ObserverState.UP_TO_DATE == _leastStaleObserverState )
    {
      _leastStaleObserverState = ObserverState.POSSIBLY_STALE;
      for ( final Observer observer : getObservers() )
      {
        if ( ObserverState.UP_TO_DATE == observer.getState() )
        {
          observer.setState( ObserverState.POSSIBLY_STALE );
        }
      }
    }
    invariantLeastStaleObserverState();
  }

  private void invariantLeastStaleObserverState()
  {
    final ObserverState leastStaleObserverState =
      getObservers().stream().
        map( Observer::getState ).min( Comparator.comparing( Enum::ordinal ) ).orElse( ObserverState.NOT_TRACKING );
    Guards.invariant( () -> leastStaleObserverState.ordinal() >= _leastStaleObserverState.ordinal(),
                      () -> String.format(
                        "Calculated leastStaleObserverState on observable named '%s' is '%s' which is unexpectedly less than cached value '%s'.",
                        getName(),
                        leastStaleObserverState.name(),
                        _leastStaleObserverState.name() ) );
  }
}
