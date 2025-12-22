package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * ComputableValue has been created.
 */
public final class ComputableValueCreateEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputableValueInfo _computableValue;

  public ComputableValueCreateEvent( @Nonnull final ComputableValueInfo computableValue )
  {
    _computableValue = Objects.requireNonNull( computableValue );
  }

  @Nonnull
  public ComputableValueInfo getComputableValue()
  {
    return _computableValue;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComputableValueCreate" );
    map.put( "name", getComputableValue().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
