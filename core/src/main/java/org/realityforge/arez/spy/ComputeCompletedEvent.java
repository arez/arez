package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Notification when Computation completes.
 */
public final class ComputeCompletedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ComputeCompletedEvent.class );

  @Nonnull
  private final ComputedValueInfo _computedValue;
  @Nonnegative
  private final long _duration;

  public ComputeCompletedEvent( @Nonnull final ComputedValueInfo computedValue, @Nonnegative final long duration )
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

  @Nonnegative
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
    map.put( "type", TYPE_NAME );
    map.put( "computedValue", getComputedValue().getName() );
    map.put( "duration", getDuration() );
  }
}
