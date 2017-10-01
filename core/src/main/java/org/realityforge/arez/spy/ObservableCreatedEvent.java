package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.Observable;

/**
 * Notification when Observable is created.
 */
public final class ObservableCreatedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObservableCreatedEvent.class );

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "observable", getObservable().getName() );
  }
}
