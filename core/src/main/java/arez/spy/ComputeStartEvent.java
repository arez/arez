package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Computation starts.
 */
public final class ComputeStartEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputableValueInfo _computableValue;

  public ComputeStartEvent( @Nonnull final ComputableValueInfo computableValue )
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
    map.put( "type", "ComputeStart" );
    map.put( "name", getComputableValue().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
