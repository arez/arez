package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.Observer;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserverApiTest
  extends AbstractArezTest
{
  @Test
  public void autorun()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Observer observer = context.autorun( name, false, () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertEquals( context.isTransactionActive(), true );
      assertEquals( context.isWriteTransactionActive(), false );
      assertEquals( context.isTrackingTransactionActive(), true );
    }, true );

    assertEquals( observer.getName(), name );
    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), true );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), false );
  }

  @Test
  public void schedule_autorun_doesNotExecuteIfNotStale()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.autorun( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    observer.schedule();

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void reportStale_schedules_autorun()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.autorun( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    context.safeAction( null, true, false, observer::reportStale );

    assertEquals( callCount.get(), 2 );

    // schedule is no-op as reportStale will have already scheduled observer
    observer.schedule();

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void schedule_tracker_will_schedule_if_and_only_if_stale()
    throws Throwable
  {
    final AtomicInteger executeCallCount = new AtomicInteger();
    final AtomicInteger scheduleCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final Observer observer = context.tracker( null, null, true, scheduleCallCount::incrementAndGet );

    assertEquals( scheduleCallCount.get(), 0 );
    assertEquals( executeCallCount.get(), 0 );

    final ObservableValue<Object> observable = context.observable();

    context.track( observer, () -> {
      observable.reportObserved();
      executeCallCount.incrementAndGet();
    } );

    assertEquals( executeCallCount.get(), 1 );
    assertEquals( scheduleCallCount.get(), 0 );

    // Schedule does nothing as observer is not stale
    observer.schedule();

    context.safeAction( observable::reportChanged );

    assertEquals( executeCallCount.get(), 1 );
    assertEquals( scheduleCallCount.get(), 1 );

    // schedule actually performs work
    observer.schedule();

    assertEquals( executeCallCount.get(), 1 );
    assertEquals( scheduleCallCount.get(), 2 );
  }
}
