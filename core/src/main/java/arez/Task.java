package arez;

import arez.spy.Priority;
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
   * The code to invoke when task is executed.
   */
  @Nonnull
  private final SafeProcedure _work;
  /**
   * State of the task.
   */
  private int _flags;

  Task( @Nullable final ArezContext context,
        @Nullable final String name,
        @Nonnull final SafeProcedure work,
        final int flags )
  {
    super( context, name );
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> ( ~Flags.TASK_FLAGS_MASK & flags ) == 0,
                    () -> "Arez-0224: Task named '" + name + "' passed invalid flags: " +
                          ( ~Flags.TASK_FLAGS_MASK & flags ) );
    }

    _work = Objects.requireNonNull( work );
    _flags = flags | Flags.STATE_IDLE | Flags.runType( flags ) | Flags.priority( flags );
  }

  int getFlags()
  {
    return _flags;
  }

  void queueTask()
  {
    getContext().getTaskQueue().queueTask( this );
  }

  void initialSchedule()
  {
    queueTask();
    triggerSchedulerInitiallyUnlessRunLater();
  }

  void triggerSchedulerInitiallyUnlessRunLater()
  {
    // If we have not explicitly supplied the RUN_LATER flag then assume it is a run now and
    // trigger the scheduler
    if ( 0 == ( _flags & Flags.RUN_LATER ) )
    {
      getContext().triggerScheduler();
    }
  }

  /**
   * Return the priority of the task.
   * This is only meaningful when TaskQueue observes priority.
   *
   * @return the priority of the task.
   */
  int getPriorityIndex()
  {
    return Flags.getPriorityIndex( _flags );
  }

  /**
   * Return the priority enum for task.
   *
   * @return the priority.
   */
  @Nonnull
  Priority getPriority()
  {
    return Priority.values()[ getPriorityIndex() ];
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

      // If this task has been marked as a task to dispose on completion then do so
      if ( 0 != ( _flags & Flags.DISPOSE_ON_COMPLETE ) )
      {
        dispose();
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
      _flags = Flags.setState( _flags, Flags.STATE_DISPOSED );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return Flags.STATE_DISPOSED == Flags.getState( _flags );
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
    _flags = Flags.setState( _flags, Flags.STATE_QUEUED );
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
    _flags = Flags.setState( _flags, Flags.STATE_IDLE );
  }

  /**
   * Return true if task is already scheduled.
   *
   * @return true if task is already scheduled.
   */
  boolean isQueued()
  {
    return Flags.STATE_QUEUED == Flags.getState( _flags );
  }

  static final class Flags
  {
    /**
     * Highest priority.
     * This priority should be used when the task will dispose or release other reactive elements
     * (and thus remove elements from being scheduled).
     *
     * <p>Only one of the PRIORITY_* flags should be applied to a task.</p>
     *
     * @see arez.annotations.Priority#HIGHEST
     * @see Priority#HIGHEST
     */
    static final int PRIORITY_HIGHEST = 0b001 << 15;
    /**
     * High priority.
     * To reduce the chance that downstream elements will react multiple times within a single
     * reaction round, this priority should be used when the observer or task may trigger many
     * downstream task.
     * <p>Only one of the PRIORITY_* flags should be applied to a task.</p>
     *
     * @see arez.annotations.Priority#HIGH
     * @see Priority#HIGH
     */
    static final int PRIORITY_HIGH = 0b010 << 15;
    /**
     * Normal priority if no other priority otherwise specified.
     *
     * <p>Only one of the PRIORITY_* flags should be applied to a task.</p>
     *
     * @see arez.annotations.Priority#NORMAL
     * @see Priority#NORMAL
     */
    static final int PRIORITY_NORMAL = 0b011 << 15;
    /**
     * Low priority.
     * Usually used to schedule observers that reflect state onto non-reactive
     * application components. i.e. Observers that are used to build html views,
     * perform network operations etc. These reactions are often at low priority
     * to avoid recalculation of dependencies (i.e. {@link ComputableValue}s) triggering
     * this reaction multiple times within a single reaction round.
     *
     * <p>Only one of the PRIORITY_* flags should be applied to a task.</p>
     *
     * @see arez.annotations.Priority#LOW
     * @see Priority#LOW
     */
    static final int PRIORITY_LOW = 0b100 << 15;
    /**
     * Lowest priority. Use this priority if the observer is a {@link ComputableValue} that
     * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
     * recomputing state that is likely to either be unobserved or recomputed as part of
     * another observers reaction.
     * <p>Only one of the PRIORITY_* flags should be applied to a task.</p>
     *
     * @see arez.annotations.Priority#LOWEST
     * @see Priority#LOWEST
     */
    static final int PRIORITY_LOWEST = 0b101 << 15;
    /**
     * Mask used to extract priority bits.
     */
    static final int PRIORITY_MASK = 0b111 << 15;
    /**
     * Shift used to extract priority after applying mask.
     */
    private static final int PRIORITY_SHIFT = 15;
    /**
     * The number of priority levels.
     */
    static final int PRIORITY_COUNT = 5;
    /**
     * The scheduler will be triggered when the task is created to immediately invoke the task.
     * This should not be specified if {@link #RUN_LATER} is specified.
     */
    static final int RUN_NOW = 1 << 22;
    /**
     * The scheduler will not be triggered when the task is created. The application is responsible
     * for ensuring thatthe  {@link ArezContext#triggerScheduler()} method is invoked at a later time.
     * This should not be specified if {@link #RUN_NOW} is specified.
     */
    static final int RUN_LATER = 1 << 21;
    /**
     * Mask used to extract run type bits.
     */
    static final int RUN_TYPE_MASK = RUN_NOW | RUN_LATER;
    /**
     * The flag that specifies that the task should be disposed after it has completed execution.
     */
    static final int DISPOSE_ON_COMPLETE = 1 << 19;
    /**
     * Mask containing flags that can be applied to a task.
     */
    static final int TASK_FLAGS_MASK = PRIORITY_MASK | RUN_TYPE_MASK | DISPOSE_ON_COMPLETE;
    /**
     * State when the task has not been scheduled.
     */
    static final int STATE_IDLE = 0;
    /**
     * State when the task has been scheduled and should not be re-scheduled until next executed.
     */
    static final int STATE_QUEUED = 1;
    /**
     * State when the task has been disposed and should no longer be scheduled.
     */
    static final int STATE_DISPOSED = 2;
    /**
     * Invalid state that should never be set.
     */
    static final int STATE_INVALID = 3;
    /**
     * Mask used to extract state bits.
     */
    private static final int STATE_MASK = STATE_IDLE | STATE_QUEUED | STATE_DISPOSED;

    /**
     * Return true if flags contains valid priority.
     *
     * @param flags the flags.
     * @return true if flags contains priority.
     */
    static boolean isStateValid( final int flags )
    {
      assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
      return STATE_INVALID != ( STATE_MASK & flags );
    }

    static int setState( final int flags, final int state )
    {
      return ( ~STATE_MASK & flags ) | state;
    }

    static int getState( final int flags )
    {
      return STATE_MASK & flags;
    }

    /**
     * Return true if flags contains a valid react type.
     *
     * @param flags the flags.
     * @return true if flags contains react type.
     */
    static boolean isRunTypeValid( final int flags )
    {
      assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
      return RUN_NOW == ( flags & RUN_NOW ) ^ RUN_LATER == ( flags & RUN_LATER );
    }

    /**
     * Return the RUN_NOW flag if run type not specified.
     *
     * @param flags the flags.
     * @return the default run type if run type unspecified else 0.
     */
    static int runType( final int flags )
    {
      return runType( flags, RUN_NOW );
    }

    /**
     * Return the default run type flag if run type not specified.
     *
     * @param flags       the flags.
     * @param defaultFlag the default flag.
     * @return the default run type if run type unspecified else 0.
     */
    static int runType( final int flags, final int defaultFlag )
    {
      return 0 == ( flags & RUN_TYPE_MASK ) ? defaultFlag : 0;
    }

    /**
     * Return true if flags contains valid priority.
     *
     * @param flags the flags.
     * @return true if flags contains priority.
     */
    static boolean isPriorityValid( final int flags )
    {
      assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
      final int priorityIndex = getPriorityIndex( flags );
      return priorityIndex <= 4 && priorityIndex >= 0;
    }

    /**
     * Extract and return the priority flag.
     * This method will not attempt to check priority value is valid.
     *
     * @param flags the flags.
     * @return the priority.
     */
    static int getPriority( final int flags )
    {
      return flags & PRIORITY_MASK;
    }

    /**
     * Extract and return the priority value ranging from the highest priority 0 and lowest priority 4.
     * This method assumes that flags has valid priority and will not attempt to re-check.
     *
     * @param flags the flags.
     * @return the priority.
     */
    static int getPriorityIndex( final int flags )
    {
      return ( getPriority( flags ) >> PRIORITY_SHIFT ) - 1;
    }

    static int priority( final int flags )
    {
      return 0 != getPriority( flags ) ? 0 : PRIORITY_NORMAL;
    }

    private Flags()
    {
    }
  }
}
