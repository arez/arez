package arez.spy;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Notification that Task is starting.
 */
public final class TaskStartEvent
  implements SerializableEvent
{
  @Nonnull
  private final TaskInfo _task;

  public TaskStartEvent( @Nonnull final TaskInfo task )
  {
    _task = Objects.requireNonNull( task );
  }

  @Nonnull
  public TaskInfo getTask()
  {
    return _task;
  }

  @Override
  public void toMap( @Nonnull final Map<String, Object> map )
  {
    map.put( "type", "TaskStart" );
    map.put( "name", getTask().getName() );
    SpyEventUtil.maybeAddZone( map );
  }
}
