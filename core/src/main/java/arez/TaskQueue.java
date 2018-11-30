package arez;

import java.util.Collection;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface representing a queue of "pending" or "scheduled" tasks.
 */
interface TaskQueue
{
  /**
   * Return the number of tasks inside the queue.
   *
   * @return the number of tasks inside the queue.
   */
  int getQueueSize();

  /**
   * Return true if queue has any tasks in it.
   *
   * @return true if queue has any tasks in it.
   */
  boolean hasTasks();

  /**
   * Remove and return the next task in queue.
   * This may return null if there is no tasks in the quue.
   *
   * @return the next task in queue.
   */
  @Nullable
  Task dequeueTask();

  /**
   * Clear all tasks from queue and return any tasks removed.
   *
   * @return tasks removed from the queue.
   */
  Collection<Task> clear();

  /**
   * Add a task to the TaskQueue.
   * The task must not be already queued.
   *
   * @param task the task.
   */
  void queueTask( @Nonnull Task task );

  /**
   * Return a stream containing tasks ordered as they would be executed.
   * This method may be very slow and should not be invoked during production compiles.
   * It is only expected to be called from invariant checking code.
   *
   * @return a stream containing tasks ordered as they would be executed.
   */
  @Nonnull
  Stream<Task> getOrderedTasks();
}
