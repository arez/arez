package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when Observer completes reaction.
 */
public final class ReactionCompletedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ReactionCompletedEvent.class );

  @Nonnull
  private final Observer _observer;
  @Nonnegative
  private final long _duration;

  public ReactionCompletedEvent( @Nonnull final Observer observer, @Nonnegative final long duration )
  {
    assert duration >= 0;
    _observer = Objects.requireNonNull( observer );
    _duration = duration;
  }

  @Nonnull
  public Observer getObserver()
  {
    return _observer;
  }

  @Nonnegative
  public long getDuration()
  {
    return _duration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "observer", getObserver().getName() );
    map.put( "duration", getDuration() );
  }
}
