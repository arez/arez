package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class Tracking
{
  /**
   * The underlying tracker.
   */
  @Nonnull
  private final Observer _observer;
  /**
   * Uniquely identifies the current execution of tracking derivation. This is cached on the
   * observables to optimize the avoidance of re-adding the same observable multiple times within
   * a single tracking execution.
   */
  private final int _id;
  /**
   * Representation of the tracking that was active when this tracking was activated. When this
   * derivation ceases to be tracked, the previous derivation will be restored.
   */
  @Nullable
  private final Tracking _previous;
  /**
   * the list of observables that have been observed during tracking.
   * This list can contain duplicates and the duplicates will be skipped when converting the list
   * of observables to dependencies in the derivation.
   */
  private final ArrayList<Observable> _observables = new ArrayList<>();

  Tracking( @Nonnull final Observer observer, final int id, @Nullable final Tracking previous )
  {
    _observer = Objects.requireNonNull( observer );
    _id = id;
    _previous = previous;
  }

  @Nonnull
  Observer getObserver()
  {
    return _observer;
  }

  int getId()
  {
    return _id;
  }

  @Nullable
  Tracking getPrevious()
  {
    return _previous;
  }

  void observe( @Nonnull final Observable observable )
  {
    /*
     * This optimization attempts to stop the same observable being added multiple
     * times to the observables list. It is purely an optimization and but this optimization
     * may be defeated if the same observable is observed in a nested tracking execution.
     */
    if ( observable.getLastTrackingId() != _id )
    {
      observable.setLastTrackingId( _id );
      _observables.add( observable );
    }
  }

  /**
   * Completes the tracking by updating the dependencies on the derivation to match the
   * observables that were observed during tracking.
   */
  final void completeTracking()
  {
    _observer.invariantDependenciesUnique();
    Guards.invariant( () -> _observer.getState() != ObserverState.NOT_TRACKING,
                      () -> "completeTracking expects derivation.dependenciesState != NOT_TRACKING" );

    ObserverState newDerivationState = ObserverState.UP_TO_DATE;

    /*
     * Iterate through the list of observables, flagging observables and removing duplicates.
     */
    final int size = _observables.size();
    int currentIndex = 0;
    for ( int i = 0; i < size; i++ )
    {
      final Observable observable = _observables.get( i );
      if ( !observable.isInCurrentTracking() )
      {
        observable.putInCurrentTracking();
      }
      if ( i != currentIndex )
      {
        _observables.set( currentIndex, observable );
      }
      currentIndex++;

      final Observer observer = observable.getObserver();
      if ( null != observer )
      {
        final ObserverState dependenciesState = observer.getState();
        if ( dependenciesState.ordinal() < newDerivationState.ordinal() )
        {
          newDerivationState = dependenciesState;
        }
      }
    }

    // Look through the old dependencies and any that are no longer tracked
    // should no longer be observed.
    final ArrayList<Observable> dependencies = _observer.getDependencies();
    for ( int i = dependencies.size() - 1; i >= 0; i-- )
    {
      final Observable observable = dependencies.get( i );
      if ( !observable.isInCurrentTracking() )
      {
        // Old dependency was not part of tracking and needs to be unobserved
        observable.removeObserver( _observer );
      }
    }

    // Look through the new observables and any that are still flagged must be
    // new dependencies and need to be observed by the derivation
    for ( int i = currentIndex - 1; i >= 0; i-- )
    {
      final Observable observable = _observables.get( i );
      if ( observable.isInCurrentTracking() )
      {
        observable.removeFromCurrentTracking();
        //Observable was not a dependency so it needs to be observed
        observable.addObserver( _observer );
      }
    }

    // Some new observed derivations may become stale during this derivation computation
    // so they have had no chance to propagate staleness
    if ( ObserverState.UP_TO_DATE != newDerivationState )
    {
      _observer.setState( newDerivationState );
    }
  }
}
