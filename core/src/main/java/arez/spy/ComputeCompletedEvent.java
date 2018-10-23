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
  private final int _duration;

  public ComputeCompletedEvent( @Nonnull final ComputedValueInfo computedValue, final int duration )
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

  public int getDuration()
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
    map.put( "name", getComputedValue().getName() );
    map.put( "duration", getDuration() );
  }
}
