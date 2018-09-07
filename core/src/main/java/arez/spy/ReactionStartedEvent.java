package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Observer starts reaction to some changes.
 */
public final class ReactionStartedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;

  public ReactionStartedEvent( @Nonnull final ObserverInfo observer )
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
    map.put( "type", "ReactionStarted" );
    map.put( "observer", getObserver().getName() );
  }
}
