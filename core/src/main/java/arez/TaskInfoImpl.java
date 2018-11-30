package arez;

import arez.spy.Priority;
import arez.spy.TaskInfo;
import java.util.Objects;
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isScheduled()
  {
    return _task.isQueued();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Priority getPriority()
  {
    return _task.getPriority();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _task.isDisposed();
  }

  /**
   * {@inheritDoc}
   */
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
