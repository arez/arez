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
   * Add the specified task to the queue.
   * The task must not already be in the queue.
   *
   * @param task the task.
   */
  void queueTask( @Nonnull Observer task );

  /**
   * Remove and return the next task in queue.
   * This may return null if there is no tasks in the quue.
   *
   * @return the next task in queue.
   */
  @Nullable
  Observer dequeueTask();

  /**
   * Clear all tasks from queue and return any tasks removed.
   *
   * @return tasks removed from the queue.
   */
  Collection<Observer> clear();

  /**
   * Return a stream containing tasks ordered as they would be executed.
   * This method may be very slow and should not be invoked during production compiles.
   * It is only expected to be called from invariant checking code.
   *
   * @return a stream containing tasks ordered as they would be executed.
   */
  @Nonnull
  Stream<Observer> getOrderedTasks();
}
