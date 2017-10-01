package org.realityforge.arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;

/**
 * ComputedValue has activated.
 */
public final class ComputedValueActivatedEvent
  implements SerializableEvent
{
  public static final String TYPE_NAME = EventUtil.getName( ComputedValueActivatedEvent.class );

  @Nonnull
  private final ComputedValue<?> _computedValue;

  public ComputedValueActivatedEvent( @Nonnull final ComputedValue<?> computedValue )
  {
    _computedValue = Objects.requireNonNull( computedValue );
  }

  @Nonnull
  public ComputedValue<?> getComputedValue()
  {
    return _computedValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", TYPE_NAME );
    map.put( "computedValue", getComputedValue().getName() );
  }
}
