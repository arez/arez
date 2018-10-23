package arez.spy;

import arez.Arez;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when ObservableValue has changed.
 */
public final class ObservableValueChangedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ObservableValueInfo _observableValue;
  @Nullable
  private final Object _value;

  public ObservableValueChangedEvent( @Nonnull final ObservableValueInfo observableValue, @Nullable final Object value )
  {
    _observableValue = Objects.requireNonNull( observableValue );
    _value = value;
  }

  @Nonnull
  public ObservableValueInfo getObservableValue()
  {
    return _observableValue;
  }

  @Nullable
  public Object getValue()
  {
    return _value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ObservableValueChanged" );
    map.put( "name", getObservableValue().getName() );
    if ( Arez.arePropertyIntrospectorsEnabled() )
    {
      map.put( "value", getValue() );
    }
  }
}
