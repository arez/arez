package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;

/**
 * ComputedValue has de-activated.
 */
public final class ComputedValueDeactivatedEvent
{
  @Nonnull
  private final ComputedValue<?> _computedValue;

  public ComputedValueDeactivatedEvent( @Nonnull final ComputedValue<?> computedValue )
  {
    _computedValue = Objects.requireNonNull( computedValue );
  }

  @Nonnull
  public ComputedValue<?> getComputedValue()
  {
    return _computedValue;
  }
}
