package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when Observer starts reaction to some changes.
 */
public final class ReactionStartedEvent
{
  @Nonnull
  private final Observer _observer;

  public ReactionStartedEvent( @Nonnull final Observer observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Observer getObserver()
  {
    return _observer;
  }
}
