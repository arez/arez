package arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * A base class for building task executors.
 */
abstract class AbstractTaskExecutor
{
  /**
   * The task queue.
   */
  @Nonnull
  private final TaskQueue _taskQueue;

  AbstractTaskExecutor( @Nonnull final TaskQueue taskQueue )
  {
    _taskQueue = Objects.requireNonNull( taskQueue );
  }

  /**
   * Add the specified task to the queue.
   * The task must not already be in the queue.
   *
   * @param task the task.
   */
  final void scheduleTask( @Nonnull final Observer task )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !task.isScheduled(),
                 () -> "Streak-0095: Attempting to schedule task named '" + task.getName() +
                       "' when task is already scheduled." );
    }
    task.markAsScheduled();
    _taskQueue.queueTask( task );
  }

  /**
   * Return the task queue.
   *
   * @return the task queue.
   */
  @Nonnull
  final TaskQueue getTaskQueue()
  {
    return _taskQueue;
  }

  /**
   * Execute the specified task.
   *
   * @param task the task.
   */
  final void executeTask( @Nonnull final Observer task )
  {
    // It is possible that the task was executed outside the executor and
    // may no longer need to be executed. This particularly true when executing tasks
    // using the "idle until urgent" strategy.
    if ( task.isScheduled() )
    {
      task.markAsExecuted();

      try
      {
        task.invokeReaction();
      }
      catch ( final Throwable t )
      {
        //TODO: Send error to per-task or global error handler?
        //Observers currently catch error and handle internally .... is this correct?
      }
    }
  }
}
