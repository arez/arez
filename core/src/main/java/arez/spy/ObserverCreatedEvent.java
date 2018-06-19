package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Observer is created.
 */
public final class ObserverCreatedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObserverCreatedEvent.class );
  @Nonnull
  private final ObserverInfo _observer;

  public ObserverCreatedEvent( @Nonnull final ObserverInfo observer )
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
    map.put( "type", TYPE_NAME );
    map.put( "observer", getObserver().getName() );
  }
}
