package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observable;

/**
 * Notification when Observable is created.
 */
public final class ObservableCreatedEvent
{
  @Nonnull
  private final Observable _observable;

  public ObservableCreatedEvent( @Nonnull final Observable observable )
  {
    _observable = Objects.requireNonNull( observable );
  }

  @Nonnull
  public Observable getObservable()
  {
    return _observable;
  }
}
