package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.Observer;
import arez.Priority;
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
    final AtomicInteger onDepsUpdatedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.autorun( null, null, false, () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsUpdatedCallCount::incrementAndGet, Priority.NORMAL, true, false, false, true );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsUpdatedCallCount.get(), 0 );

    observer.schedule();

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsUpdatedCallCount.get(), 0 );
  }

  @Test
  public void reportStale_schedules_autorun()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.autorun( null, null, false, () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, Priority.NORMAL, true, false, true, false );

    assertEquals( callCount.get(), 1 );

    context.safeAction( null, true, false, observer::reportStale );

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void schedule_onManuallyScheduled_autorun()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger onDepsUpdatedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.autorun( null, null, false, () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsUpdatedCallCount::incrementAndGet, Priority.NORMAL, true, false, true, false );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsUpdatedCallCount.get(), 0 );

    context.safeAction( null, true, false, observer::reportStale );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsUpdatedCallCount.get(), 1 );

    observer.schedule();

    assertEquals( callCount.get(), 2 );
    assertEquals( onDepsUpdatedCallCount.get(), 1 );
  }
}
