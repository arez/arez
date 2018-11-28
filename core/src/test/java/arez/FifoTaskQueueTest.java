package arez;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class FifoTaskQueueTest
  extends AbstractArezTest
{
  @Test
  public void construct()
  {
    final FifoTaskQueue queue = new FifoTaskQueue( 20 );

    assertEquals( queue.getQueueSize(), 0 );
    assertFalse( queue.hasTasks() );
    assertEquals( queue.getBuffer().getCapacity(), 20 );
  }

  @Test
  public void queueTask_alreadyQueued()
  {
    final FifoTaskQueue queue = new FifoTaskQueue( 20 );

    final Task task = new Task( Arez.context(), "A", ValueUtil::randomString );
    queue.queueTask( task );
    assertInvariantFailure( () -> queue.queueTask( task ),
                            "Arez-0098: Attempting to queue task named 'A' when task is already queued." );
  }

  @Test
  public void basicOperation()
  {
    final FifoTaskQueue queue = new FifoTaskQueue( 100 );

    assertFalse( queue.hasTasks() );
    assertEquals( queue.getQueueSize(), 0 );
    assertNull( queue.dequeueTask() );

    final ArezContext context = Arez.context();
    queue.queueTask( new Task( context, "A", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 1 );
    assertTrue( queue.hasTasks() );
    queue.queueTask( new Task( context, "B", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 2 );
    queue.queueTask( new Task( context, "C", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 3 );
    queue.queueTask( new Task( context, "D", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 4 );
    queue.queueTask( new Task( context, "E", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 5 );
    queue.queueTask( new Task( context, "F", ValueUtil::randomString ) );
    assertEquals( queue.getQueueSize(), 6 );

    assertEquals( queue.getBuffer().size(), 6 );

    assertEquals( getTask( queue, 0 ).getName(), "A" );
    assertEquals( getTask( queue, 1 ).getName(), "B" );
    assertEquals( getTask( queue, 2 ).getName(), "C" );
    assertEquals( getTask( queue, 3 ).getName(), "D" );
    assertEquals( getTask( queue, 4 ).getName(), "E" );
    assertEquals( getTask( queue, 5 ).getName(), "F" );

    assertEquals( queue.getOrderedTasks().map( Task::getName ).collect( Collectors.joining( "," ) ), "A,B,C,D,E,F" );

    assertEquals( queue.getQueueSize(), 6 );
    assertDequeue( queue, "A" );
    assertEquals( queue.getQueueSize(), 5 );
    assertDequeue( queue, "B" );
    assertEquals( queue.getQueueSize(), 4 );
    assertDequeue( queue, "C" );
    assertEquals( queue.getQueueSize(), 3 );

    assertEquals( queue.getOrderedTasks().map( Task::getName ).collect( Collectors.joining( "," ) ), "D,E,F" );

    assertDequeue( queue, "D" );
    assertEquals( queue.getQueueSize(), 2 );
    assertDequeue( queue, "E" );
    assertEquals( queue.getQueueSize(), 1 );
    assertTrue( queue.hasTasks() );
    assertDequeue( queue, "F" );
    assertEquals( queue.getQueueSize(), 0 );
    assertFalse( queue.hasTasks() );

    assertNull( queue.dequeueTask() );
    assertEquals( queue.getQueueSize(), 0 );
  }

  @Nonnull
  private Task getTask( @Nonnull final FifoTaskQueue queue, final int index )
  {
    final Task task = queue.getBuffer().get( index );
    assertNotNull( task );
    return task;
  }

  @Test
  public void clear()
  {
    final FifoTaskQueue queue = new FifoTaskQueue( 20 );

    final ArezContext context = Arez.context();
    queue.queueTask( new Task( context, "A", ValueUtil::randomString ) );
    queue.queueTask( new Task( context, "B", ValueUtil::randomString ) );
    queue.queueTask( new Task( context, "C", ValueUtil::randomString ) );
    queue.queueTask( new Task( context, "D", ValueUtil::randomString ) );
    queue.queueTask( new Task( context, "E", ValueUtil::randomString ) );
    queue.queueTask( new Task( context, "F", ValueUtil::randomString ) );

    assertEquals( queue.getQueueSize(), 6 );

    final Collection<Task> tasks = queue.clear();

    assertEquals( queue.getQueueSize(), 0 );

    assertEquals( tasks.stream().map( Task::getName ).collect( Collectors.joining( "," ) ), "A,B,C,D,E,F" );
  }

  private void assertDequeue( @Nonnull final FifoTaskQueue queue, @Nonnull final String name )
  {
    final Task task = queue.dequeueTask();
    assertNotNull( task );
    assertEquals( task.getName(), name );
  }
}
