package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * ComputableValue has activated.
 */
public final class ComputableValueActivateEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputableValueInfo _computableValue;

  public ComputableValueActivateEvent( @Nonnull final ComputableValueInfo computableValue )
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
    map.put( "type", "ComputableValueActivate" );
    map.put( "name", getComputableValue().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
