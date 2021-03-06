package arez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A queue of "pending" or "scheduled" tasks that supports priority based queuing of tasks.
 */
final class TaskQueue
{
  /**
   * A buffer per priority containing tasks that have been scheduled but are not executing.
   */
  @Nonnull
  private final CircularBuffer<Task>[] _buffers;

  /**
   * Construct queue with specified priority count where each priority is backed by a buffer with specified size.
   *
   * @param priorityCount   the number of priorities supported.
   * @param initialCapacity the initial size of buffer for each priority.
   */
  @SuppressWarnings( { "unchecked", "rawtypes", "RedundantSuppression" } )
  TaskQueue( final int priorityCount, final int initialCapacity )
  {
    assert priorityCount > 0;
    assert initialCapacity > 0;
    _buffers = (CircularBuffer<Task>[]) new CircularBuffer[ priorityCount ];
    for ( int i = 0; i < priorityCount; i++ )
    {
      _buffers[ i ] = new CircularBuffer<>( initialCapacity );
    }
  }

  /**
   * Return the number of priorities handled by the queue.
   *
   * @return the number of priorities handled by the queue.
   */
  int getPriorityCount()
  {
    return _buffers.length;
  }

  /**
   * Return the number of tasks inside the queue.
   *
   * @return the number of tasks inside the queue.
   */
  int getQueueSize()
  {
    int count = 0;
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      count += _buffers[ i ].size();
    }
    return count;
  }

  /**
   * Return true if queue has any tasks in it.
   *
   * @return true if queue has any tasks in it.
   */
  boolean hasTasks()
  {
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      if ( _buffers[ i ].isNotEmpty() )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Add the specified task to the queue.
   * The task must not already be in the queue.
   *
   * @param task the task.
   */
  void queueTask( @Nonnull final Task task )
  {
    queueTask( task.getPriorityIndex(), task );
  }

  /**
   * Add the specified task to the queue.
   * The task must not already be in the queue.
   *
   * @param priority the task priority.
   * @param task     the task.
   */
  void queueTask( final int priority, @Nonnull final Task task )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arrays.stream( _buffers ).noneMatch( b -> b.contains( task ) ),
                 () -> "Arez-0099: Attempting to queue task named '" + task.getName() +
                       "' when task is already queued." );
      invariant( () -> priority >= 0 && priority < _buffers.length,
                 () -> "Arez-0215: Attempting to queue task named '" + task.getName() +
                       "' but passed an invalid priority " + priority + "." );
    }
    Objects.requireNonNull( task ).markAsQueued();
    _buffers[ priority ].add( Objects.requireNonNull( task ) );
  }

  /**
   * Remove and return the next task in queue.
   * This may return null if there is no tasks in the quue.
   *
   * @return the next task in queue.
   */
  @Nullable
  Task dequeueTask()
  {
    // Return the highest priority taskQueue that has tasks in it and return task.
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      final CircularBuffer<Task> taskQueue = _buffers[ i ];
      if ( taskQueue.isNotEmpty() )
      {
        final Task task = taskQueue.pop();
        assert null != task;
        return task;
      }
    }
    return null;
  }

  /**
   * Clear all tasks from queue and return any tasks removed.
   *
   * @return tasks removed from the queue.
   */
  @Nonnull
  Collection<Task> clear()
  {
    final List<Task> tasks = new ArrayList<>();
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      final CircularBuffer<Task> buffer = _buffers[ i ];
      Task task;
      while ( null != ( task = buffer.pop() ) )
      {
        tasks.add( task );
        task.markAsIdle();
      }
    }
    return tasks;
  }

  /**
   * Return a stream containing tasks ordered as they would be executed.
   * This method may be very slow and should not be invoked during production compiles.
   * It is only expected to be called from invariant checking code.
   *
   * @return a stream containing tasks ordered as they would be executed.
   */
  @Nonnull
  Stream<Task> getOrderedTasks()
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    return Stream.of( _buffers ).flatMap( CircularBuffer::stream );
  }

  @Nonnull
  CircularBuffer<Task> getBufferByPriority( final int priority )
  {
    return _buffers[ priority ];
  }
}
