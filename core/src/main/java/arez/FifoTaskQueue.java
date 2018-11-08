package arez;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A very simple first-in first out task queue.
 */
final class FifoTaskQueue
  implements TaskQueue
{
  /**
   * The default number of slots in queue.
   */
  private static final int DEFAULT_QUEUE_SIZE = 100;
  /**
   * A buffer per priority containing tasks that have been scheduled but are not executing.
   */
  @Nonnull
  private final CircularBuffer<Observer> _taskQueue;

  FifoTaskQueue()
  {
    this( DEFAULT_QUEUE_SIZE );
  }

  FifoTaskQueue( final int queueSize )
  {
    _taskQueue = new CircularBuffer<>( queueSize );
  }

  @Override
  public int getQueueSize()
  {
    return _taskQueue.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasTasks()
  {
    return !_taskQueue.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void queueTask( @Nonnull final Observer task )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      invariant( () -> !_taskQueue.contains( task ),
                 () -> "Arez-0089: Attempting to schedule task named '" + task.getName() +
                       "' when task is already in queue." );
    }
    _taskQueue.add( Objects.requireNonNull( task ) );
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public Observer dequeueTask()
  {
    return _taskQueue.pop();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<Observer> clear()
  {
    final ArrayList<Observer> tasks = new ArrayList<>();
    _taskQueue.stream().forEach( tasks::add );
    _taskQueue.clear();
    return tasks;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public Stream<Observer> getOrderedTasks()
  {
    return _taskQueue.stream();
  }
}
