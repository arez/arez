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

    assertFalse( task.isScheduled() );
    assertFalse( task.isDisposed() );

    // Mark scheduling so we see that dispose flips schedule flag
    task.markAsScheduled();

    assertTrue( task.isScheduled() );
    assertFalse( task.isDisposed() );

    task.dispose();

    assertFalse( task.isScheduled() );
    assertTrue( task.isDisposed() );

    // Second dispose is effectively a no-op
    task.dispose();

    assertFalse( task.isScheduled() );
    assertTrue( task.isDisposed() );
  }

  @Test
  public void noNameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final Runnable work = ValueUtil::randomString;
    final Task task = new Task( null, work );
    assertTrue( task.toString().startsWith( task.getClass().getName() + "@" ), "task.toString() == " + task );

    assertInvariantFailure( task::getName,
                            "Arez-0214: Task.getName() invoked when Arez.areNamesEnabled() returns false" );
  }

  @Test
  public void nameSuppliedWhenNamesDisabled()
  {
    ArezTestUtil.disableNames();

    final String name = ValueUtil.randomString();
    final Runnable work = ValueUtil::randomString;
    assertInvariantFailure( () -> new Task( name, work ),
                            "Arez-0130: Task passed a name '" + name + "' but Arez.areNamesEnabled() returns false" );
  }

  @Test
  public void scheduleFlag()
  {
    final Task task = new Task( ValueUtil.randomString(), ValueUtil::randomString );

    assertFalse( task.isScheduled() );

    task.markAsScheduled();

    assertTrue( task.isScheduled() );

    task.markAsExecuted();

    assertFalse( task.isScheduled() );
  }

  @Test
  public void markAsScheduled_alreadyScheduled()
  {
    final Task task = new Task( ValueUtil.randomString(), ValueUtil::randomString );

    task.markAsScheduled();
    assertInvariantFailure( task::markAsScheduled,
                            "Arez-0128: Attempting to re-schedule task named '" + task.getName() +
                            "' when task is already scheduled." );

  }

  @Test
  public void markAsExecuted_notScheduled()
  {
    final Task task = new Task( ValueUtil.randomString(), ValueUtil::randomString );

    assertInvariantFailure( task::markAsExecuted,
                            "Arez-0129: Attempting to clear scheduled flag on task named '" + task.getName() +
                            "' but task is not scheduled." );

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

    task.markAsScheduled();

    assertEquals( callCount.get(), 0 );
    assertTrue( task.isScheduled() );

    task.executeTask();

    assertEquals( callCount.get(), 1 );
    assertFalse( task.isScheduled() );
  }
}
