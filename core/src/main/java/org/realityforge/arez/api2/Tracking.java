package org.realityforge.arez.api2;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class Tracking
{
  /**
   * The underlying derivation that is being tracked.
   */
  @Nonnull
  private final Derivation _derivation;
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
   */
  private final ArrayList<Observable> _observables = new ArrayList<>();

  Tracking( @Nonnull final Derivation derivation, final int id, @Nullable final Tracking previous )
  {
    _derivation = Objects.requireNonNull( derivation );
    _id = id;
    _previous = previous;
  }

  @Nonnull
  Derivation getDerivation()
  {
    return _derivation;
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

  ArrayList<Observable> getObservables()
  {
    return _observables;
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
      final ArrayList<Observable> observables = getObservables();
      //OPT: Can this contains() call be omitted? What happens if there are duplicates in Observables list?
      // If it is needed can it be optimized it with a map+array pair?
      if ( observables.contains( observable ) )
      {
        observable.setLastTrackingId( _id );
        observables.add( observable );
      }
    }
  }
}
