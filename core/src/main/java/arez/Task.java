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
  private boolean _queued;
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
    if ( isQueued() )
    {
      markAsDequeued();

      // Observers currently catch error and handle internally. Thus no need to catch
      // errors here. is this correct behaviour? We could instead handle it here by
      // per-task handler or a global error handler.
      _work.run();
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
      _queued = false;
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
   * Mark task as being queued, first verifying that it is not already queued.
   * This is used so that task will not be able to be queued again until it has run.
   */
  void markAsQueued()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !_queued,
                 () -> "Arez-0128: Attempting to re-queue task named '" + getName() +
                       "' when task is queued." );
    }
    _queued = true;
  }

  /**
   * Clear the queued flag, first verifying that the task is queued.
   */
  void markAsDequeued()
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> _queued,
                 () -> "Arez-0129: Attempting to clear queued flag on task named '" + getName() +
                       "' but task is not queued." );
    }
    _queued = false;
  }

  /**
   * Return true if task is already scheduled.
   *
   * @return true if task is already scheduled.
   */
  boolean isQueued()
  {
    return _queued;
  }
}
