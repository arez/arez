package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Computation completes.
 */
public final class ComputeCompletedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputedValueInfo _computedValue;
  private final long _duration;

  public ComputeCompletedEvent( @Nonnull final ComputedValueInfo computedValue, final long duration )
  {
    assert duration >= 0;
    _computedValue = Objects.requireNonNull( computedValue );
    _duration = duration;
  }

  @Nonnull
  public ComputedValueInfo getComputedValue()
  {
    return _computedValue;
  }

  public long getDuration()
  {
    return _duration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComputeCompleted" );
    map.put( "computed", getComputedValue().getName() );
    map.put( "duration", getDuration() );
  }
}
