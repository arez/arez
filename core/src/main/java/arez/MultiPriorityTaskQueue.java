package arez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Basic implementation of task queue that supports priority based queuing of tasks.
 */
final class MultiPriorityTaskQueue
  implements TaskQueue
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
  @SuppressWarnings( "unchecked" )
  MultiPriorityTaskQueue( final int priorityCount, final int initialCapacity )
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
   * {@inheritDoc}
   */
  @Override
  public int getQueueSize()
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
   * {@inheritDoc}
   */
  @Override
  public boolean hasTasks()
  {
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      if ( !_buffers[ i ].isEmpty() )
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
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Task dequeueTask()
  {
    // Return the highest priority taskQueue that has tasks in it and return task.
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      final CircularBuffer<Task> taskQueue = _buffers[ i ];
      if ( !taskQueue.isEmpty() )
      {
        final Task task = taskQueue.pop();
        assert null != task;
        return task;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Task> clear()
  {
    final ArrayList<Task> tasks = new ArrayList<>();
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _buffers.length; i++ )
    {
      final CircularBuffer<Task> buffer = _buffers[ i ];
      buffer.stream().forEach( task -> {
        tasks.add( task );
        task.markAsDequeued();
      } );
      buffer.clear();
    }
    return tasks;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Stream<Task> getOrderedTasks()
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
