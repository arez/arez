package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification when Computation completes.
 */
public final class ComputeCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final ComputableValueInfo _computableValue;
  @Nullable
  private final Object _result;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public ComputeCompleteEvent( @Nonnull final ComputableValueInfo computableValue,
                               @Nullable final Object result,
                               @Nullable final Throwable throwable,
                               final int duration )
  {
    assert duration >= 0;
    assert null == throwable || null == result;
    _computableValue = Objects.requireNonNull( computableValue );
    _result = result;
    _throwable = throwable;
    _duration = duration;
  }

  @Nonnull
  public ComputableValueInfo getComputableValue()
  {
    return _computableValue;
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

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "ComputeComplete" );
    map.put( "name", getComputableValue().getName() );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    map.put( "result", getResult() );
    SpyEventUtil.maybeAddZone( map );
  }
}
