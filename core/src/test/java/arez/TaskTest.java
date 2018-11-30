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
    final Runnable work = ValueUtil::randomString;
    final Task task = new Task( name, work );

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
  public void noNameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final Task task = new Task( null, ValueUtil::randomString );
    assertTrue( task.toString().startsWith( task.getClass().getName() + "@" ), "task.toString() == " + task );

    assertInvariantFailure( task::getName,
                            "Arez-0214: Task.getName() invoked when Arez.areNamesEnabled() returns false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> new Task( name, ValueUtil::randomString ),
                            "Arez-0130: Task passed a name '" + name + "' but Arez.areNamesEnabled() returns false" );
  }

  @Test
  public void queuedFlag()
  {
    final Task task = new Task( ValueUtil.randomString(), ValueUtil::randomString );

    assertFalse( task.isQueued() );

    task.markAsQueued();

    assertTrue( task.isQueued() );

    task.markAsDequeued();

    assertFalse( task.isQueued() );
  }

  @Test
  public void markAsQueued_alreadyScheduled()
  {
    final Task task = new Task( "X", ValueUtil::randomString );

    task.markAsQueued();
    assertInvariantFailure( task::markAsQueued,
                            "Arez-0128: Attempting to re-queue task named 'X' when task is queued." );

  }

  @Test
  public void markAsExecuted_notScheduled()
  {
    final Task task = new Task( "X", ValueUtil::randomString );

    assertInvariantFailure( task::markAsDequeued,
                            "Arez-0129: Attempting to clear queued flag on task named 'X' but task is not queued." );

  }

  @Test
  public void executeTask_notScheduled()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task = new Task( ValueUtil.randomString(), callCount::incrementAndGet );

    assertEquals( callCount.get(), 0 );

    // Invoked executeTask but not scheduled.
    task.executeTask();

    assertEquals( callCount.get(), 0 );
  }

  @Test
  public void executeTask_scheduled()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task = new Task( ValueUtil.randomString(), callCount::incrementAndGet );

    task.markAsQueued();

    assertEquals( callCount.get(), 0 );
    assertTrue( task.isQueued() );

    task.executeTask();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isQueued() );
  }
}