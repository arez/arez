package arez.component;

import arez.Arez;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Interface implemented by a component if it supports notifying listeners
 * when the component is disposed. This notification occurs using a call-back
 * and occurs within the dispose transaction (after {@link arez.annotations.PreDispose}
 * is invoked if present) using a callback. Contrast this with the strategy used by
 * {@link ComponentObservable} which uses standard Arez observables to tracke when
 * a component is disposed.
 */
public interface DisposeTrackable
{
  /**
   * Return the notifier associated with the component.
   *
   * @return the notifier associated with the component.
   */
  @Nonnull
  DisposeNotifier getNotifier();

  /**
   * Return the notifier associated with the supplied object if object implements DisposeTrackable.
   *
   * @param object the object.
   * @return the notifier associated with the supplied object if object implements DisposeTrackable.
   */
  @Nullable
  static DisposeNotifier getNotifier( @Nonnull final Object object )
  {
    return object instanceof DisposeTrackable ? ( (DisposeTrackable) object ).getNotifier() : null;
  }

  /**
   * Cast the specified object to an instance of DisposeTrackable.
   * Invariant checks will verify that the cast is valid before proceeding.
   *
   * @param object the object.
   * @return the object cast to DisposeTrackable.
   */
  @Nonnull
  static DisposeTrackable asDisposeTrackable( @Nonnull final Object object )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> object instanceof DisposeTrackable,
                    () -> "Arez-0178: Object passed to asDisposeTrackable does not implement " +
                          "DisposeTrackable. Object: " + object );
    }
    return (DisposeTrackable) object;
  }
}
