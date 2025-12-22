package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when ObservableValue is created.
 */
public final class ObservableValueCreateEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObservableValueInfo _observableValue;

  public ObservableValueCreateEvent( @Nonnull final ObservableValueInfo observableValue )
  {
    _observableValue = Objects.requireNonNull( observableValue );
  }

  @Nonnull
  public ObservableValueInfo getObservableValue()
  {
    return _observableValue;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObservableValueCreate" );
    map.put( "name", getObservableValue().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
