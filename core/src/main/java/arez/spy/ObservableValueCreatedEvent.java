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
  @Nonnull
  private final ObservableValueInfo _observableValue;

  public ObservableValueCreatedEvent( @Nonnull final ObservableValueInfo observableValue )
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
    map.put( "type", "ObservableValueCreated" );
    map.put( "name", getObservableValue().getName() );
  }
}
