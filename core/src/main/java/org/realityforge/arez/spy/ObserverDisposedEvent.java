package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observer;

/**
 * Notification when Observer is disposed.
 */
public final class ObserverDisposedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObserverDisposedEvent.class );

  @Nonnull
  private final Observer _observer;

  public ObserverDisposedEvent( @Nonnull final Observer observer )
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
