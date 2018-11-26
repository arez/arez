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
   * Flag set to true when the task has been scheduled and should not be re-scheduled until next executed.
   */
  private static final int QUEUED = 1 << 1;
  /**
   * Flag set to true when the task has been disposed and should no longer be scheduled.
   */
  private static final int DISPOSED = 2 << 1;
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
  private final SafeProcedure _work;
  /**
   * Flags representing state of the task.
   */
  private int _flags;

  Task( @Nullable final String name, @Nonnull final SafeProcedure work )
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
      _flags |= DISPOSED;
      _flags &= ~QUEUED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return 0 != ( _flags & DISPOSED );
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
    _flags |= QUEUED;
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
    _flags &= ~QUEUED;
  }

  /**
   * Return true if task is already scheduled.
   *
   * @return true if task is already scheduled.
   */
  boolean isQueued()
  {
    return 0 != ( _flags & QUEUED );
  }
}
