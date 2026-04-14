package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Observer is disposed.
 */
public final class ObserverDisposeEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObserverInfo _observer;

  public ObserverDisposeEvent( @Nonnull final ObserverInfo observer )
  {
    _observer = Objects.requireNonNull( observer );
  }

  @Nonnull
  public ObserverInfo getObserver()
  {
    return _observer;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObserverDispose" );
    map.put( "name", getObserver().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
