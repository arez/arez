package arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A task represents an executable element that can be ran by a task executor.
 */
final class Task
  implements Disposable
{
  /**
   * A human consumable name for task. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * The code to invoke when task is executed.
   */
  @Nonnull
  private final Runnable _work;
  /**
   * Flag set to true when the task has been scheduled and should not be re-scheduled until next executed.
   */
  private boolean _scheduled;
  /**
   * Flag set to true when the task has been disposed and should no longer be scheduled.
   */
  private boolean _disposed;

  Task( @Nullable final String name, @Nonnull final Runnable work )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                    () -> "Arez-0130: Task passed a name '" + name + "' but Arez.areNamesEnabled() returns false" );
    }
    _name = name;
    _work = Objects.requireNonNull( work );
  }

  /**
   * Return the name of the task.
   * This method should NOT be invoked unless {@link Arez#areNamesEnabled()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the task.
   */
  @Nonnull
  final String getName()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNamesEnabled,
                    () -> "Arez-0214: Task.getName() invoked when Arez.areNamesEnabled() returns false" );
    }
    assert null != _name;
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String toString()
  {
    if ( Arez.areNamesEnabled() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }

  /**
   * Return the task.
   *
   * @return the task.
   */
  @Nonnull
  Runnable getWork()
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
    if ( isScheduled() )
    {
      markAsExecuted();

      try
      {
        getWork().run();
      }
      catch ( final Throwable t )
      {
        //TODO: Send error to per-task or global error handler?
        //Observers currently catch error and handle internally .... is this correct?
      }
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
      _disposed = true;
      _scheduled = false;
      //TODO:_scheduler.cancelTask( this );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }

  /**
   * Mark task as being scheduled, first verifying that it is not already scheduled.
   * This is used so that task will not be able to be scheduled again until it has run.
   */
  void markAsScheduled()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !_scheduled,
                 () -> "Arez-0128: Attempting to re-schedule task named '" + getName() +
                       "' when task is already scheduled." );
    }
    _scheduled = true;
  }

  /**
   * Clear the scheduled flag, first verifying that the task is scheduled.
   */
  void markAsExecuted()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _scheduled,
                 () -> "Arez-0129: Attempting to clear scheduled flag on task named '" + getName() +
                       "' but task is not scheduled." );
    }
    _scheduled = false;
  }

  /**
   * Return true if task is already scheduled.
   *
   * @return true if task is already scheduled.
   */
  boolean isScheduled()
  {
    return _scheduled;
  }
}
