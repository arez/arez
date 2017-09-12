package org.realityforge.arez.spy;

import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.realityforge.arez.ComputedValue;

/**
 * Notification when Computation completes.
 */
public final class ComputeCompletedEvent
{
  @Nonnull
  private final ComputedValue<?> _computedValue;
  @Nonnegative
  private final long _duration;

  public ComputeCompletedEvent( @Nonnull final ComputedValue<?> computedValue, @Nonnegative final long duration )
  {
    assert duration >= 0;
    _computedValue = Objects.requireNonNull( computedValue );
    _duration = duration;
  }

  @Nonnull
  public ComputedValue<?> getComputedValue()
  {
    return _computedValue;
  }

  @Nonnegative
  public long getDuration()
  {
    return _duration;
  }
}
