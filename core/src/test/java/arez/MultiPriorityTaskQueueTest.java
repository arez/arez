package arez;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MultiPriorityTaskQueueTest
  extends AbstractArezTest
{
  @Test
  public void construct()
  {
    final MultiPriorityTaskQueue queue = new MultiPriorityTaskQueue( 3, 22 );

    assertEquals( queue.getPriorityCount(), 3 );
    assertEquals( queue.getBufferByPriority( 0 ).getCapacity(), 22 );
  }

  @Test
  public void queueTask_badPriority()
  {
    final MultiPriorityTaskQueue queue = new MultiPriorityTaskQueue( 3, 10 );

    final ArezContext context = Arez.context();
    assertInvariantFailure( () -> queue.queueTask( -1, context.task( "A", ValueUtil::randomString ) ),
                            "Arez-0215: Attempting to queue task named 'A' but passed an invalid priority -1." );
    assertInvariantFailure( () -> queue.queueTask( 77, context.task( "B", ValueUtil::randomString ) ),
                            "Arez-0215: Attempting to queue task named 'B' but passed an invalid priority 77." );
  }

  @Test
  public void queueTask_direct_alreadyQueued()
  {
    final MultiPriorityTaskQueue queue = new MultiPriorityTaskQueue( 3, 10 );

    final ArezContext context = Arez.context();
    final Task task = context.task( "A", ValueUtil::randomString, Task.Flags.RUN_LATER );
    task.markAsDequeued();
    queue.queueTask( 0, task );
    task.markAsDequeued();
    assertInvariantFailure( () -> queue.queueTask( 2, task ),
                            "Arez-0099: Attempting to queue task named 'A' when task is already queued." );
  }

  @Test
  public void queueTask_alreadyQueued()
  {
    final MultiPriorityTaskQueue queue = new MultiPriorityTaskQueue( 3, 10 );

    final ArezContext context = Arez.context();
    final Task task = context.task( "A", ValueUtil::randomString, Task.Flags.RUN_LATER );
    assertInvariantFailure( () -> queue.queueTask( task ),
                            "Arez-0128: Attempting to re-queue task named 'A' when task is queued." );
  }

  @Test
  public void basicOperation()
  {
    final ArezContext context = Arez.context();

    final MultiPriorityTaskQueue queue = context.getTaskQueue();

    assertFalse( queue.hasTasks() );
    assertEquals( queue.getQueueSize(), 0 );
    assertNull( queue.dequeueTask() );

    context.task( "A", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGHEST );
    assertEquals( queue.getQueueSize(), 1 );
    assertTrue( queue.hasTasks() );
    context.task( "B", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );
    assertEquals( queue.getQueueSize(), 2 );
    context.task( "C", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOWEST );
    assertEquals( queue.getQueueSize(), 3 );
    context.task( "D", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOWEST );
    assertEquals( queue.getQueueSize(), 4 );
    context.task( "E", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_NORMAL );
    assertEquals( queue.getQueueSize(), 5 );
    context.task( "F", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );
    assertEquals( queue.getQueueSize(), 6 );

    assertEquals( queue.getBufferByPriority( 0 ).size(), 1 );
    assertEquals( queue.getBufferByPriority( 1 ).size(), 2 );
    assertEquals( queue.getBufferByPriority( 2 ).size(), 1 );
    assertEquals( queue.getBufferByPriority( 3 ).size(), 0 );
    assertEquals( queue.getBufferByPriority( 4 ).size(), 2 );

    assertEquals( getTask( queue, 0, 0 ).getName(), "A" );
    assertEquals( getTask( queue, 1, 0 ).getName(), "B" );
    assertEquals( getTask( queue, 1, 1 ).getName(), "F" );
    assertEquals( getTask( queue, 2, 0 ).getName(), "E" );
    assertEquals( getTask( queue, 4, 0 ).getName(), "C" );
    assertEquals( getTask( queue, 4, 1 ).getName(), "D" );

    assertEquals( queue.getOrderedTasks().map( Task::getName ).collect( Collectors.joining( "," ) ), "A,B,F,E,C,D" );

    assertEquals( queue.getQueueSize(), 6 );
    assertDequeue( queue, "A" );
    assertEquals( queue.getQueueSize(), 5 );
    assertDequeue( queue, "B" );
    assertEquals( queue.getQueueSize(), 4 );
    assertDequeue( queue, "F" );
    assertEquals( queue.getQueueSize(), 3 );

    assertEquals( queue.getOrderedTasks().map( Task::getName ).collect( Collectors.joining( "," ) ), "E,C,D" );

    assertDequeue( queue, "E" );
    assertEquals( queue.getQueueSize(), 2 );
    assertDequeue( queue, "C" );
    assertEquals( queue.getQueueSize(), 1 );
    assertTrue( queue.hasTasks() );
    assertDequeue( queue, "D" );
    assertEquals( queue.getQueueSize(), 0 );
    assertFalse( queue.hasTasks() );

    assertNull( queue.dequeueTask() );
    assertEquals( queue.getQueueSize(), 0 );
  }

  @Test
  public void clear()
  {
    final ArezContext context = Arez.context();
    final MultiPriorityTaskQueue queue = context.getTaskQueue();

    context.task( "A", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGHEST );
    context.task( "B", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );
    context.task( "C", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOWEST );
    context.task( "D", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_LOWEST );
    context.task( "E", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_NORMAL );
    context.task( "F", ValueUtil::randomString, Task.Flags.RUN_LATER | Task.Flags.PRIORITY_HIGH );

    assertEquals( queue.getQueueSize(), 6 );

    final Collection<Task> tasks = queue.clear();

    assertEquals( queue.getQueueSize(), 0 );

    assertEquals( tasks.stream().map( Task::getName ).collect( Collectors.joining( "," ) ), "A,B,F,E,C,D" );
  }

  private void assertDequeue( @Nonnull final MultiPriorityTaskQueue queue, @Nonnull final String name )
  {
    final Task task = queue.dequeueTask();
    assertNotNull( task );
    assertEquals( task.getName(), name );
  }

  @Nonnull
  private Task getTask( @Nonnull final MultiPriorityTaskQueue queue, final int priority, final int index )
  {
    final Task task = queue.getBufferByPriority( priority ).get( index );
    assertNotNull( task );
    return task;
  }
}
