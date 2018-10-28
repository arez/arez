package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Observer is about to call method being observed.
 */
public final class ObserveStartedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;

  public ObserveStartedEvent( @Nonnull final ObserverInfo observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public ObserverInfo getObserver()
  {
    return _observer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObserveStarted" );
    map.put( "name", getObserver().getName() );
  }
}