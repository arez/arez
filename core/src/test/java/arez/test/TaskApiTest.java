package arez.test;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.Observer;
import arez.SchedulerLock;
import arez.Task;
import arez.TestSpyEventHandler;
import arez.spy.TaskCompleteEvent;
import arez.spy.TaskStartEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class TaskApiTest
  extends AbstractTest
{
  @Test
  public void task()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, callCount::incrementAndGet, 0 );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getTask().getName(), name );
      assertNull( e.getThrowable() );
      assertTrue( e.getDuration() >= 0 );
    } );
  }

  @Test
  public void task_withError()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, () -> {
      callCount.incrementAndGet();
      throw new IllegalStateException( "Foo" );
    }, 0 );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertFalse( task.isDisposed() );

    handler.unsubscribe();

    handler.assertEventCount( 2 );
    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getTask().getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getTask().getName(), name );
      assertNotNull( e.getThrowable() );
      assertEquals( e.getThrowable().getMessage(), "Foo" );
      assertTrue( e.getDuration() >= 0 );
    } );
  }

  @Test
  public void task_DISPOSE_ON_COMPLETE()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final Task task = context.task( callCount::incrementAndGet, Task.Flags.DISPOSE_ON_COMPLETE );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertTrue( task.isDisposed() );
  }

  @Test
  public void schedule()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final Task task = Arez.context().task( callCount::incrementAndGet );

    assertEquals( callCount.get(), 1 );

    task.schedule();

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void schedule_whileSchedulerPaused()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final ArezContext context = Arez.context();
    final SchedulerLock schedulerLock = context.pauseScheduler();

    final Task task = context.task( callCount::incrementAndGet );

    assertEquals( callCount.get(), 0 );

    // Reschedule ... should be a no-op
    task.schedule();

    assertEquals( callCount.get(), 0 );

    schedulerLock.dispose();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void schedule_insideTask()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final ArezContext context = Arez.context();
    final AtomicReference<Task> taskRef = new AtomicReference<>();
    context.task( () -> {
      taskRef.set( context.task( callCount::incrementAndGet ) );

      assertEquals( callCount.get(), 0 );

      // Reschedule ... should be a no-op
      taskRef.get().schedule();

      assertEquals( callCount.get(), 0 );
    } );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void schedule_insideObserver()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final ArezContext context = Arez.context();
    final AtomicReference<Task> taskRef = new AtomicReference<>();
    context.observer( () -> {
      taskRef.set( context.task( callCount::incrementAndGet ) );
      assertEquals( callCount.get(), 0 );
    }, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );
  }
}
