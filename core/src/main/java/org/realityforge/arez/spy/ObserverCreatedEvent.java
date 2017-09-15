package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when Observer is created.
 */
public final class ObserverCreatedEvent
{
  @Nonnull
  private final Observer _observer;

  public ObserverCreatedEvent( @Nonnull final Observer observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Observer getObserver()
  {
    return _observer;
  }
}
