package arez;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
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
   * The default number of priorities.
   */
  private static final int DEFAULT_PRIORITY_COUNT = 5;
  /**
   * The default of slots in each priority buffer.
   */
  private static final int DEFAULT_BUFFER_SIZE = 100;
  /**
   * A buffer per priority containing tasks that have been scheduled but are not executing.
   */
  @Nonnull
  private final CircularBuffer<Observer>[] _taskQueues;
  @Nonnull
  private final Function<Observer, Integer> _priorityMapper;

  /**
   * Construct queue with priority count specified by {@link #DEFAULT_PRIORITY_COUNT} where each priority is backed by a buffer with default size specified by {@link #DEFAULT_BUFFER_SIZE}.
   *
   * @param priorityMapper the function that maps task to priority.
   */
  MultiPriorityTaskQueue( @Nonnull final Function<Observer, Integer> priorityMapper )
  {
    this( DEFAULT_PRIORITY_COUNT, priorityMapper );
  }

  /**
   * Construct queue with specified priority count where each priority is backed by a buffer with default size specified by {@link #DEFAULT_BUFFER_SIZE}.
   *
   * @param priorityCount  the number of priorities supported.
   * @param priorityMapper the function that maps task to priority.
   */
  private MultiPriorityTaskQueue( final int priorityCount, @Nonnull final Function<Observer, Integer> priorityMapper )
  {
    this( priorityCount, priorityMapper, DEFAULT_BUFFER_SIZE );
  }

  /**
   * Construct queue with specified priority count where each priority is backed by a buffer with specified size.
   *
   * @param priorityCount  the number of priorities supported.
   * @param priorityMapper the function that maps task to priority.
   * @param bufferSize     the initial size of buffer for each priority.
   */
  @SuppressWarnings( "unchecked" )
  private MultiPriorityTaskQueue( final int priorityCount,
                                  @Nonnull final Function<Observer, Integer> priorityMapper,
                                  final int bufferSize )
  {
    assert priorityCount > 0;
    assert bufferSize > 0;
    _taskQueues = (CircularBuffer<Observer>[]) new CircularBuffer[ priorityCount ];
    for ( int i = 0; i < priorityCount; i++ )
    {
      _taskQueues[ i ] = new CircularBuffer<>( bufferSize );
    }
    _priorityMapper = Objects.requireNonNull( priorityMapper );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getQueueSize()
  {
    int count = 0;
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _taskQueues.length; i++ )
    {
      count += _taskQueues[ i ].size();
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
    for ( int i = 0; i < _taskQueues.length; i++ )
    {
      if ( !_taskQueues[ i ].isEmpty() )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void queueTask( @Nonnull final Observer task )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> Arrays.stream( _taskQueues ).noneMatch( b -> b.contains( task ) ),
                 () -> "Arez-0099: Attempting to schedule task named '" + task.getName() +
                       "' when task is already in queues." );
    }
    _taskQueues[ _priorityMapper.apply( task ) ].add( Objects.requireNonNull( task ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Observer dequeueTask()
  {
    // Return the highest priority taskQueue that has tasks in it and return task.
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _taskQueues.length; i++ )
    {
      final CircularBuffer<Observer> taskQueue = _taskQueues[ i ];
      if ( !taskQueue.isEmpty() )
      {
        final Observer task = taskQueue.pop();
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
  public Collection<Observer> clear()
  {
    final ArrayList<Observer> tasks = new ArrayList<>();
    //noinspection ForLoopReplaceableByForEach
    for ( int i = 0; i < _taskQueues.length; i++ )
    {
      final CircularBuffer<Observer> taskQueue = _taskQueues[ i ];
      taskQueue.stream().forEach( tasks::add );
      taskQueue.clear();
    }
    return tasks;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Stream<Observer> getOrderedTasks()
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    return Stream.of( _taskQueues ).flatMap( CircularBuffer::stream );
  }

  @Nonnull
  CircularBuffer<Observer> getTasksByPriority( final int priority )
  {
    return _taskQueues[ priority ];
  }
}
