package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when reaction scheduled.
 */
public final class ReactionScheduledEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ReactionScheduledEvent.class );
  @Nonnull
  private final ObserverInfo _observer;

  public ReactionScheduledEvent( @Nonnull final ObserverInfo observer )
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
