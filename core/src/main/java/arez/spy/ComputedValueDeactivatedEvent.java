package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * ComputedValue has de-activated.
 */
public final class ComputedValueDeactivatedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputedValueInfo _computedValue;

  public ComputedValueDeactivatedEvent( @Nonnull final ComputedValueInfo computedValue )
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
    map.put( "type", "ComputedValueDeactivated" );
    map.put( "computed", getComputedValue().getName() );
  }
}
