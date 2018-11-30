package arez.spy;

import javax.annotation.Nonnull;

/**
 * A representation of a task instance exposed to spy framework.
 */
public interface TaskInfo
  extends ElementInfo
{
  /**
   * Return true if the task is idle - not scheduled to run and not disposed.
   *
   * @return true if the task is idle.
   */
  boolean isIdle();

  /**
   * Return true if the task is scheduled to run.
   *
   * @return true if the task is scheduled to run.
   */
  boolean isScheduled();

  /**
   * Return the priority of the task.
   *
   * @return the priority of the task.
   */
  @Nonnull
  Priority getPriority();
}
