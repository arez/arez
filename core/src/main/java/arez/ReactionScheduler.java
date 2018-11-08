package arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * The scheduler is responsible for scheduling observer reactions.
 */
final class ReactionScheduler
{
  @Nullable
  private final ArezContext _context;
  /**
   * Scheduled tasks yet to be run.
   */
  @Nonnull
  private final MultiPriorityTaskQueue _taskQueue;
  /**
   * Executor responsible for executing tasks.
   */
  @Nonnull
  private final RoundBasedTaskExecutor _executor;
  /**
   * Elements that should be disposed prior to next reaction being invoked.
   * Disposes are often scheduled when they can not happen immediately as the transaction is not READ_WRITE.
   * i.e. A disposeOnDeactivate component may no longer have any observers when a ComputableValue no longer
   * references it but can not dispose from within the ComputableValue's transaction as it is in a READ_WRITE_OWNED
   * transactions.
   */
  @Nonnull
  private final CircularBuffer<Disposable> _pendingDisposes = new CircularBuffer<>( 100 );
  /**
   * Flag set when processing disposes.
   */
  private boolean _disposesRunning;

  @SuppressWarnings( "unchecked" )
  ReactionScheduler( @Nullable final ArezContext context )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      apiInvariant( () -> Arez.areZonesEnabled() || null == context,
                    () -> "Arez-0164: ReactionScheduler passed a context but Arez.areZonesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _taskQueue = new MultiPriorityTaskQueue( Observer::getPriorityIndex );
    _executor = new RoundBasedTaskExecutor( _taskQueue );
  }

  /**
   * Return the task queue associated with the scheduler.
   *
   * @return the task queue associated with the scheduler.
   */
  @Nonnull
  MultiPriorityTaskQueue getTaskQueue()
  {
    return _taskQueue;
  }

  /**
   * Return the executor responsible for executing tasks.
   *
   * @return the executor responsible for executing tasks.
   */
  @Nonnull
  RoundBasedTaskExecutor getExecutor()
  {
    return _executor;
  }

  /**
   * Add the specified disposable to the list of pending disposables.
   * The disposable must not already be in the list of pending observers.
   *
   * @param disposable the disposable.
   */
  void scheduleDispose( @Nonnull final Disposable disposable )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !_pendingDisposes.contains( disposable ),
                 () -> "Arez-0165: Attempting to schedule disposable '" + disposable +
                       "' when disposable is already pending." );
    }
    _pendingDisposes.add( Objects.requireNonNull( disposable ) );
  }

  /**
   * Return true if disposes are currently being disposed, false otherwise.
   *
   * @return true if disposes are currently being disposed, false otherwise.
   */
  boolean areDisposesRunning()
  {
    return _disposesRunning;
  }

  /**
   * If the schedule is not already running pending tasks then run pending observers until
   * complete or runaway reaction detected.
   */
  void runPendingTasks()
  {
    /*
     * All disposes expect to run as top level transactions so
     * there should be no transaction active.
     */
    if ( Arez.shouldCheckInvariants() )
    {
      final ArezContext context = getContext();
      invariant( () -> !context.isTransactionActive(),
                 () -> "Arez-0100: Invoked runPendingTasks() when transaction named '" +
                       context.getTransaction().getName() + "' is active." );
    }

    //TODO: getExecutor().runTasks();
    while ( true )
    {
      if ( !runDispose() && !getExecutor().runNextTask() )
      {
        break;
      }
    }
  }

  /**
   * Dispose next pending disposable if any.
   *
   * @return true if a dispose occurred, false otherwise.
   */
  boolean runDispose()
  {
    /*
     * All disposes expect to run as top level transactions so
     * there should be no transaction active.
     */
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !getContext().isTransactionActive(),
                 () -> "Arez-0156: Invoked runDispose when transaction named '" +
                       getContext().getTransaction().getName() + "' is active." );
    }
    if ( 0 == _pendingDisposes.size() )
    {
      _disposesRunning = false;
      return false;
    }
    else
    {
      _disposesRunning = true;
      final Disposable disposable = _pendingDisposes.pop();
      assert null != disposable;
      Disposable.dispose( disposable );
      return true;
    }
  }

  boolean hasTasksToSchedule()
  {
    return !_pendingDisposes.isEmpty() || _taskQueue.hasTasks();
  }

  @Nonnull
  ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  @Nonnull
  CircularBuffer<Disposable> getPendingDisposes()
  {
    return _pendingDisposes;
  }
}
