package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Computation completes.
 */
public final class ComputeCompletedEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputedValueInfo _computedValue;
  @Nullable
  private final Object _result;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public ComputeCompletedEvent( @Nonnull final ComputedValueInfo computedValue,
                                @Nullable final Object result,
                                @Nullable final Throwable throwable,
                                final int duration )
  {
    assert duration >= 0;
    assert null == throwable || null == result;
    _computedValue = Objects.requireNonNull( computedValue );
    _result = result;
    _throwable = throwable;
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

  @Nullable
  public Object getResult()
  {
    return _result;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
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
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    map.put( "result", getResult() );
  }
}
