package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when ObservableValue is created.
 */
public final class ObservableValueCreatedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObservableValueCreatedEvent.class );
  @Nonnull
  private final ObservableValueInfo _observable;

  public ObservableValueCreatedEvent( @Nonnull final ObservableValueInfo observable )
  {
    _observable = Objects.requireNonNull( observable );
  }

  @Nonnull
  public ObservableValueInfo getObservable()
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
