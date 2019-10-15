package arez;

import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A lock that stops the scheduler from running tasks until the lock is released.
 */
public final class SchedulerLock
  implements Disposable
{
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nullable
  private final ArezContext _context;
  /**
   * True if already released.
   */
  private boolean _released;

  SchedulerLock( @Nullable final ArezContext context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arez.areZonesEnabled() || null == context,
                 () -> "Arez-0174: SchedulerLock passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
  }

  @Override
  public void dispose()
  {
    if ( isNotDisposed() )
    {
      _released = true;
      getContext().releaseSchedulerLock();
    }
  }

  @Override
  public boolean isDisposed()
  {
    return _released;
  }

  @Nonnull
  private ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }
}
