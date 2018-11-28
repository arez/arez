package arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A task represents an executable element that can be ran by a task executor.
 */
final class Task
  extends Node
{
  /**
   * State when the task has not been scheduled.
   */
  private static final int IDLE = 0;
  /**
   * State when the task has been scheduled and should not be re-scheduled until next executed.
   */
  private static final int QUEUED = 1;
  /**
   * State when the task has been disposed and should no longer be scheduled.
   */
  private static final int DISPOSED = 2;
  /**
   * The code to invoke when task is executed.
   */
  @Nonnull
  private final SafeProcedure _work;
  /**
   * State of the task.
   */
  private int _state;

  Task( @Nullable final ArezContext context, @Nullable final String name, @Nonnull final SafeProcedure work )
  {
    super( context, name );
    _work = Objects.requireNonNull( work );
  }

  /**
   * Return the task.
   *
   * @return the task.
   */
  @Nonnull
  SafeProcedure getWork()
  {
    return _work;
  }

  /**
   * Execute the work associated with the task.
   */
  void executeTask()
  {
    // It is possible that the task was executed outside the executor and
    // may no longer need to be executed. This particularly true when executing tasks
    // using the "idle until urgent" strategy.
    if ( isQueued() )
    {
      markAsDequeued();

      // Observers currently catch error and handle internally. Thus no need to catch
      // errors here. is this correct behaviour? We could instead handle it here by
      // per-task handler or a global error handler.
      _work.call();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    if ( isNotDisposed() )
    {
      _state = DISPOSED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return DISPOSED == _state;
  }

  /**
   * Mark task as being queued, first verifying that it is not already queued.
   * This is used so that task will not be able to be queued again until it has run.
   */
  void markAsQueued()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !isQueued(),
                 () -> "Arez-0128: Attempting to re-queue task named '" + getName() +
                       "' when task is queued." );
    }
    _state = QUEUED;
  }

  /**
   * Clear the queued flag, first verifying that the task is queued.
   */
  void markAsDequeued()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( this::isQueued,
                 () -> "Arez-0129: Attempting to clear queued flag on task named '" + getName() +
                       "' but task is not queued." );
    }
    _state = IDLE;
  }

  /**
   * Return true if task is already scheduled.
   *
   * @return true if task is already scheduled.
   */
  boolean isQueued()
  {
    return QUEUED == _state;
  }
}
