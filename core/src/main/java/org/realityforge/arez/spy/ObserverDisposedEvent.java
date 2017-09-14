package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Node;

/**
 * Notification when Observer is disposed.
 */
public final class ObserverDisposedEvent
{
  @Nonnull
  private final Node _observer;

  public ObserverDisposedEvent( @Nonnull final Node observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Node getObserver()
  {
    return _observer;
  }
}
