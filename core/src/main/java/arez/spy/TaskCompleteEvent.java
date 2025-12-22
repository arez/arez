package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Notification that task has completed execution.
 */
public final class TaskCompleteEvent
  implements SerializableEvent
{
  @Nonnull
  private final TaskInfo _task;
  @Nullable
  private final Throwable _throwable;
  private final int _duration;

  public TaskCompleteEvent( @Nonnull final TaskInfo task,
                            @Nullable final Throwable throwable,
                            final int duration )
  {
    assert duration >= 0;
    _task = Objects.requireNonNull( task );
    _throwable = throwable;
    _duration = duration;
  }

  @Nonnull
  public TaskInfo getTask()
  {
    return _task;
  }

  @Nullable
  public Throwable getThrowable()
  {
    return _throwable;
  }

  public int getDuration()
  {
    return _duration;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "TaskComplete" );
    map.put( "name", getTask().getName() );
    map.put( "duration", getDuration() );
    final Throwable throwable = getThrowable();
    final String message =
      null == throwable ? null : null == throwable.getMessage() ? throwable.toString() : throwable.getMessage();
    map.put( "errorMessage", message );
    SpyEventUtil.maybeAddZone( map );
  }
}
