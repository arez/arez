package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Arez;
import org.realityforge.arez.Observable;

/**
 * Notification when Observable has changed.
 */
public final class ObservableChangedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ObservableChangedEvent.class );

  @Nonnull
  private final Observable<?> _observable;
  @Nullable
  private final Object _value;

  public ObservableChangedEvent( @Nonnull final Observable<?> observable, @Nullable final Object value )
  {
    _observable = Objects.requireNonNull( observable );
    _value = value;
  }

  @Nonnull
  public Observable<?> getObservable()
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
