package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Node;

/**
 * Notification when Observer starts reaction to some changes.
 */
public final class ReactionStartedEvent
{
  @Nonnull
  private final Node _observer;

  public ReactionStartedEvent( @Nonnull final Node observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Node getObserver()
  {
    return _observer;
  }
}
