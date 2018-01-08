package arez.spy;

import arez.Arez;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Observable has changed.
 */
public final class ObservableChangedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObservableChangedEvent.class );

  @Nonnull
  private final ObservableInfo _observable;
  @Nullable
  private final Object _value;

  public ObservableChangedEvent( @Nonnull final ObservableInfo observable, @Nullable final Object value )
  {
    _observable = Objects.requireNonNull( observable );
    _value = value;
  }

  @Nonnull
  public ObservableInfo getObservable()
  {
    return _observable;
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
    map.put( "type", TYPE_NAME );
    map.put( "observable", getObservable().getName() );
    if ( Arez.arePropertyIntrospectorsEnabled() )
    {
      map.put( "value", getValue() );
    }
  }
}
