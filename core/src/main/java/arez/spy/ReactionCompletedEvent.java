package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Observer completes reaction.
 */
public final class ReactionCompletedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;
  private final int _duration;

  public ReactionCompletedEvent( @Nonnull final ObserverInfo observer, final int duration )
  {
    assert duration >= 0;
    _observer = Objects.requireNonNull( observer );
    _duration = duration;
  }

  @Nonnull
  public ObserverInfo getObserver()
  {
    return _observer;
  }

  public int getDuration()
  {
    return _duration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ReactionCompleted" );
    map.put( "name", getObserver().getName() );
    map.put( "duration", getDuration() );
  }
}
