package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Node;

/**
 * Notification when reaction scheduled.
 */
public final class ReactionScheduledEvent
{
  @Nonnull
  private final Node _observer;

  public ReactionScheduledEvent( @Nonnull final Node observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Node getObserver()
  {
    return _observer;
  }
}
