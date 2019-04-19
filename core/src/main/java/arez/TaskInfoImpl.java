package arez;

import arez.spy.Priority;
import arez.spy.TaskInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * A implementation of {@link TaskInfo} that proxies to a {@link Task}.
 */
final class TaskInfoImpl
  implements TaskInfo
{
  private final Task _task;

  TaskInfoImpl( @Nonnull final Task task )
  {
    _task = Objects.requireNonNull( task );
  }

  @Nonnull
  private static List<TaskInfo> asInfos( @Nonnull final Collection<Task> tasks )
  {
    return tasks
      .stream()
      .map( Task::asInfo )
      .collect( Collectors.toList() );
  }

  @Nonnull
  static List<TaskInfo> asUnmodifiableInfos( @Nonnull final Collection<Task> tasks )
  {
    return Collections.unmodifiableList( asInfos( tasks ) );
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _task.getName();
  }

  @Override
  public boolean isIdle()
  {
    return _task.isIdle();
  }

  @Override
  public boolean isScheduled()
  {
    return _task.isQueued();
  }

  @Nonnull
  @Override
  public Priority getPriority()
  {
    return _task.getPriority();
  }

  @Override
  public boolean isDisposed()
  {
    return _task.isDisposed();
  }

  @Override
  public String toString()
  {
    return _task.toString();
  }

  @Override
  public boolean equals( final Object o )
  {
    if ( this == o )
    {
      return true;
    }
    else if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }
    else
    {
      final TaskInfoImpl that = (TaskInfoImpl) o;
      return _task.equals( that._task );
    }
  }

  @Override
  public int hashCode()
  {
    return _task.hashCode();
  }
}
