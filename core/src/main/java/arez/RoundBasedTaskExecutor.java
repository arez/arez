package arez;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import static org.realityforge.braincheck.Guards.*;

/**
 * This executor runs tasks in rounds.
 * At the start of the round, the number of tasks currently queued is recorded and the executor runs that
 * number of tasks. There may be tasks remaining at the end of the round, as running a task may result in
 * one or more tasks being scheduled. The executor may have a round budget and if it exceeds the round budget
 * will stop running tasks and optionally emptying the task queue.
 */
final class RoundBasedTaskExecutor
{
  /**
   * The task queue.
   */
  @Nonnull
  private final TaskQueue _taskQueue;
  /**
   * The maximum number of iterations that can be triggered in sequence without triggering an error. Set this
   * to 0 to disable check, otherwise trigger
   */
  private final int _maxRounds;
  /**
   * The current round.
   */
  private int _currentRound;
  /**
   * The number of tasks left in the current round.
   */
  private int _remainingTasksInCurrentRound;

  RoundBasedTaskExecutor( @Nonnull final TaskQueue taskQueue, final int maxRounds )
  {
    assert maxRounds > 0;
    _taskQueue = Objects.requireNonNull( taskQueue );
    _maxRounds = maxRounds;
  }

  /**
   * Return the maximum number of rounds before runaway task is detected.
   *
   * @return the maximum number of rounds.
   */
  int getMaxRounds()
  {
    return _maxRounds;
  }

  /**
   * Return true if tasks are currently executing, false otherwise.
   *
   * @return true if tasks are currently executing, false otherwise.
   */
  boolean areTasksExecuting()
  {
    return 0 != _currentRound;
  }

  /**
   * Run tasks until complete or runaway tasks detected.
   */
  void runTasks()
  {
    while ( true )
    {
      if ( !runNextTask() )
      {
        break;
      }
    }
  }

  /**
   * Execute the next task if any.
   * <ul>
   * <li>If there is any reactions left in this round then run the next reaction and consume a token.</li>
   * <li> If there are more rounds left in budget and more pending tasks then start a new round,
   * allocating a number of tokens equal to the number of pending tasks, run the next task
   * and consume a token.</li>
   * <li>Otherwise runaway tasks are detected, so act appropriately.</li>
   * </ul>
   *
   * @return true if a task was ran, false otherwise.
   */
  boolean runNextTask()
  {
    // If we have reached the last task in this round then
    // determine if we need any more rounds and if we do ensure
    if ( 0 == _remainingTasksInCurrentRound )
    {
      final int pendingTaskCount = getPendingTaskCount();
      if ( 0 == pendingTaskCount )
      {
        _currentRound = 0;
        return false;
      }
      else if ( _currentRound + 1 > _maxRounds )
      {
        _currentRound = 0;
        onRunawayTasksDetected();
        return false;
      }
      else
      {
        _currentRound = _currentRound + 1;
        _remainingTasksInCurrentRound = pendingTaskCount;
      }
    }
    /*
     * If we get to here there are still tasks that need processing and we have not
     * exceeded our round budget. So we pop a task off the list and process it.
     *
     * The first task is chosen as the same task will only be executed multiple times
     * per round if there is no higher priority tasks and there is some lower priority
     * tasks. This means that when runaway task detection code is active, the list of
     * pending tasks starts with those tasks that have likely lead to the runaway condition.
     */
    _remainingTasksInCurrentRound--;

    final Task task = _taskQueue.dequeueTask();
    assert null != task;
    task.executeTask();
    return true;
  }

  int getPendingTaskCount()
  {
    return _taskQueue.getQueueSize();
  }

  /**
   * Called when runaway tasks detected.
   * Depending on configuration will optionally purge the pending
   * tasks and optionally fail an invariant check.
   */
  void onRunawayTasksDetected()
  {
    final List<String> taskNames =
      Arez.shouldCheckInvariants() ?
      _taskQueue.getOrderedTasks().map( Task::getName ).collect( Collectors.toList() ) :
      null;

    if ( Arez.purgeTasksWhenRunawayDetected() )
    {
      _taskQueue.clear();
    }

    if ( Arez.shouldCheckInvariants() )
    {
      fail( () -> "Arez-0101: Runaway task(s) detected. Tasks still running after " + _maxRounds +
                  " rounds. Current tasks include: " + taskNames );
    }
  }

  int getCurrentRound()
  {
    return _currentRound;
  }

  int getRemainingTasksInCurrentRound()
  {
    return _remainingTasksInCurrentRound;
  }
}
