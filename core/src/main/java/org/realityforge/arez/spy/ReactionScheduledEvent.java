package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when reaction scheduled.
 */
public final class ReactionScheduledEvent
{
  @Nonnull
  private final Observer _observer;

  public ReactionScheduledEvent( @Nonnull final Observer observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Observer getObserver()
  {
    return _observer;
  }
}
