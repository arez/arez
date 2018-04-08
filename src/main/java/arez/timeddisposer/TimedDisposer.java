package arez.timeddisposer;

import arez.Arez;
import arez.Disposable;
import elemental2.dom.DomGlobal;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * Utility class that will dispose specified target after a delay.
 * This is particularly useful when combined with other reactive primitives
 * that allows those primitives to be decommissioned timeout after a timeout.
 */
public final class TimedDisposer
  implements Disposable
{
  private final Disposable _target;
  private double _timeoutId;

  /**
   * Created the TimedDisposer.
   *
   * @param target  the element to dispose on timeout.
   * @param timeout the time to wait before disposing component.
   * @return the new timed disposer.
   */
  @Nonnull
  public static TimedDisposer create( @Nonnull final Disposable target,
                                      @Nonnegative final long timeout )
  {
    return new TimedDisposer( target, timeout );
  }

  private TimedDisposer( @Nonnull final Disposable target,
                         @Nonnegative final long timeout )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> timeout >= 0,
                    () -> "Arez-0164: TimedDisposer passed an invalid timeout. Expected positive number " +
                          "but actual value: " + timeout );
    }
    _target = Objects.requireNonNull( target );
    _timeoutId = DomGlobal.setTimeout( e -> performDispose( false ), timeout );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return 0 == _timeoutId;
  }

  /**
   * Dispose the target element.
   */
  @Override
  public void dispose()
  {
    performDispose( true );
  }

  private void performDispose( final boolean cancelTimeout )
  {
    if ( cancelTimeout && 0 != _timeoutId )
    {
      DomGlobal.clearTimeout( _timeoutId );
      _timeoutId = 0;
    }
    Disposable.dispose( _target );
  }
}
