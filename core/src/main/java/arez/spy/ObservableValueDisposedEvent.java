package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when ObservableValue is disposed.
 */
public final class ObservableValueDisposedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObservableValueDisposedEvent.class );
  @Nonnull
  private final ObservableValueInfo _observable;

  public ObservableValueDisposedEvent( @Nonnull final ObservableValueInfo observable )
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
