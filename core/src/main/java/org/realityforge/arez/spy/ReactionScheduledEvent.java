package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when reaction scheduled.
 */
public final class ReactionScheduledEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ReactionScheduledEvent.class );

  @Nonnull
  private final Observer _observer;

  public ReactionScheduledEvent( @Nonnull final Observer observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public Observer getObserver()
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
