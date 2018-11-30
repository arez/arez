package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.Flags;
import arez.TestSpyEventHandler;
import arez.spy.TaskCompleteEvent;
import arez.spy.TaskStartEvent;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TaskApiTest
  extends AbstractArezTest
{
  @Test
  public void task()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, callCount::incrementAndGet, 0 );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
      assertNull( e.getThrowable() );
      assertTrue( e.getDuration() >= 0 );
    } );
  }

  @Test
  public void task_withError()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, () -> {
      callCount.incrementAndGet();
      throw new IllegalStateException( "Foo" );
    }, 0 );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertFalse( task.isDisposed() );

    handler.assertEventCount( 2 );
    handler.assertNextEvent( TaskStartEvent.class, e -> assertEquals( e.getName(), name ) );
    handler.assertNextEvent( TaskCompleteEvent.class, e -> {
      assertEquals( e.getName(), name );
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

    final String name = ValueUtil.randomString();
    final Task task = context.task( name, callCount::incrementAndGet, Flags.DISPOSE_ON_COMPLETE );

    assertEquals( callCount.get(), 1 );
    assertNotNull( task );
    assertTrue( task.isDisposed() );
  }
}
