package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification when Computation starts.
 */
public final class ComputeStartedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputedValueInfo _computedValue;

  public ComputeStartedEvent( @Nonnull final ComputedValueInfo computedValue )
  {
    _computedValue = Objects.requireNonNull( computedValue );
  }

  @Nonnull
  public ComputedValueInfo getComputedValue()
  {
    return _computedValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComputeStarted" );
    map.put( "name", getComputedValue().getName() );
  }
}
