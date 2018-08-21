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
  private final ObservableValueInfo _observableValue;

  public ObservableValueDisposedEvent( @Nonnull final ObservableValueInfo observableValue )
  {
    _observableValue = Objects.requireNonNull( observableValue );
  }

  @Nonnull
  public ObservableValueInfo getObservableValue()
  {
    return _observableValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "observable", getObservableValue().getName() );
  }
}
