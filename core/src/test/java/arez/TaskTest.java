package arez;

import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskTest
  extends AbstractArezTest
{
  @Test
  public void basicOperation()
  {
    final String name = ValueUtil.randomString();
    final SafeProcedure work = ValueUtil::randomString;
    final Task task = new Task( Arez.context(), name, work, 0 );

    assertEquals( task.getName(), name );
    assertEquals( task.getWork(), work );

    assertEquals( task.toString(), name );

    assertFalse( task.isQueued() );
    assertFalse( task.isDisposed() );

    // Mark scheduling so we see that dispose flips schedule flag
    task.markAsQueued();

    assertTrue( task.isQueued() );
    assertFalse( task.isDisposed() );

    task.dispose();

    assertFalse( task.isQueued() );
    assertTrue( task.isDisposed() );

    // Second dispose is effectively a no-op
    task.dispose();

    assertFalse( task.isQueued() );
    assertTrue( task.isDisposed() );
  }

  @Test
  public void queuedFlag()
  {
    final Task task = new Task( Arez.context(), ValueUtil.randomString(), ValueUtil::randomString, 0 );

    assertFalse( task.isQueued() );

    task.markAsQueued();

    assertTrue( task.isQueued() );

    task.markAsDequeued();

    assertFalse( task.isQueued() );
  }

  @Test
  public void markAsQueued_alreadyScheduled()
  {
    final Task task = new Task( Arez.context(), "X", ValueUtil::randomString, 0 );

    task.markAsQueued();
    assertInvariantFailure( task::markAsQueued,
                            "Arez-0128: Attempting to re-queue task named 'X' when task is queued." );

  }

  @Test
  public void markAsExecuted_notScheduled()
  {
    final Task task = new Task( Arez.context(), "X", ValueUtil::randomString, 0 );

    assertInvariantFailure( task::markAsDequeued,
                            "Arez-0129: Attempting to clear queued flag on task named 'X' but task is not queued." );

  }

  @Test
  public void executeTask_notScheduled()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task = new Task( Arez.context(), ValueUtil.randomString(), callCount::incrementAndGet, 0 );

    assertEquals( callCount.get(), 0 );

    // Invoked executeTask but not scheduled.
    task.executeTask();

    assertEquals( callCount.get(), 0 );
    assertFalse( Disposable.isDisposed( task ) );
  }

  @Test
  public void executeTask_scheduled()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task = new Task( Arez.context(), ValueUtil.randomString(), callCount::incrementAndGet, 0 );

    task.markAsQueued();

    assertEquals( callCount.get(), 0 );
    assertTrue( task.isQueued() );

    task.executeTask();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertFalse( Disposable.isDisposed( task ) );
  }

  @Test
  public void executeTask_DISPOSE_ON_COMPLETE()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task =
      new Task( Arez.context(), ValueUtil.randomString(), callCount::incrementAndGet, Task.Flags.DISPOSE_ON_COMPLETE );

    task.markAsQueued();

    assertEquals( callCount.get(), 0 );
    assertTrue( task.isQueued() );

    task.executeTask();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
    assertTrue( Disposable.isDisposed( task ) );
  }

  @Test
  public void isStateValid()
  {
    assertTrue( Task.Flags.isStateValid( Task.Flags.RUN_LATER ) );
    assertTrue( Task.Flags.isStateValid( Task.Flags.STATE_IDLE | Task.Flags.RUN_LATER ) );
    assertTrue( Task.Flags.isStateValid( Task.Flags.STATE_QUEUED | Task.Flags.RUN_LATER ) );
    assertTrue( Task.Flags.isStateValid( Task.Flags.STATE_DISPOSED | Task.Flags.RUN_LATER ) );
    assertFalse( Task.Flags.isStateValid( Task.Flags.STATE_INVALID | Task.Flags.RUN_LATER ) );
  }

  @Test
  public void getPriority()
  {
    assertEquals( Task.Flags.getPriorityIndex( Task.Flags.PRIORITY_HIGHEST | Task.Flags.STATE_QUEUED ), 0 );
    assertEquals( Task.Flags.getPriorityIndex( Task.Flags.PRIORITY_HIGH | Task.Flags.STATE_QUEUED ), 1 );
    assertEquals( Task.Flags.getPriorityIndex( Task.Flags.PRIORITY_NORMAL | Task.Flags.STATE_QUEUED ), 2 );
    assertEquals( Task.Flags.getPriorityIndex( Task.Flags.PRIORITY_LOW | Task.Flags.STATE_QUEUED ), 3 );
    assertEquals( Task.Flags.getPriorityIndex( Task.Flags.PRIORITY_LOWEST | Task.Flags.STATE_QUEUED ), 4 );
  }

  @Test
  public void isRunTypeValid()
  {
    assertTrue( Task.Flags.isRunTypeValid( Task.Flags.RUN_NOW ) );
    assertTrue( Task.Flags.isRunTypeValid( Task.Flags.RUN_LATER ) );
    assertFalse( Task.Flags.isRunTypeValid( 0 ) );
    assertFalse( Task.Flags.isRunTypeValid( Task.Flags.PRIORITY_LOWEST ) );
    assertFalse( Task.Flags.isRunTypeValid( Task.Flags.RUN_NOW | Task.Flags.RUN_LATER ) );
  }

  @Test
  public void runType()
  {
    assertEquals( Task.Flags.runType( Task.Flags.RUN_NOW, Task.Flags.RUN_NOW ), 0 );
    assertEquals( Task.Flags.runType( Task.Flags.RUN_LATER, Task.Flags.RUN_NOW ), 0 );
    assertEquals( Task.Flags.runType( 0, Task.Flags.RUN_NOW ), Task.Flags.RUN_NOW );
    assertEquals( Task.Flags.runType( Task.Flags.STATE_QUEUED, Task.Flags.RUN_NOW ), Task.Flags.RUN_NOW );
  }

  @Test
  public void priority()
  {
    assertEquals( Task.Flags.priority( Task.Flags.PRIORITY_HIGHEST ), 0 );
    assertEquals( Task.Flags.priority( Task.Flags.PRIORITY_HIGH ), 0 );
    assertEquals( Task.Flags.priority( Task.Flags.PRIORITY_NORMAL ), 0 );
    assertEquals( Task.Flags.priority( Task.Flags.PRIORITY_LOW ), 0 );
    assertEquals( Task.Flags.priority( Task.Flags.PRIORITY_LOWEST ), 0 );
    assertEquals( Task.Flags.priority( 0 ), Task.Flags.PRIORITY_NORMAL );
    assertEquals( Task.Flags.priority( Task.Flags.STATE_QUEUED ), Task.Flags.PRIORITY_NORMAL );
  }
}
