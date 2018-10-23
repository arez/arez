package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * ComputedValue has activated.
 */
public final class ComputedValueActivatedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputedValueInfo _computedValue;

  public ComputedValueActivatedEvent( @Nonnull final ComputedValueInfo computedValue )
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
    map.put( "type", "ComputedValueActivated" );
    map.put( "name", getComputedValue().getName() );
  }
}
