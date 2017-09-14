package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Node;

/**
 * Notification when Observer is created.
 */
public final class ObserverCreatedEvent
{
  @Nonnull
  private final Node _observer;

  public ObserverCreatedEvent( @Nonnull final Node observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Node getObserver()
  {
    return _observer;
  }
}
