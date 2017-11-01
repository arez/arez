package org.realityforge.arez;

import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * A lock that stop the scheduler from reacting to any changes until the lock is released.
 * This is principally used so that users can control
 */
final class SchedulerLock
  implements Disposable
{
  private final ArezContext _context;
  /**
   * True if already released.
   */
  private boolean _released;

  SchedulerLock( @Nonnull final ArezContext context )
  {
    _context = Objects.requireNonNull( context );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    if ( !isDisposed() )
    {
      _released = true;
      _context.releaseSchedulerLock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _released;
  }
}
