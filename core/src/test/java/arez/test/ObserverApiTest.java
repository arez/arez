package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.Flags;
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
    final Observer observer = context.observer( name, () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertEquals( context.isTransactionActive(), true );
      assertEquals( context.isReadWriteTransactionActive(), false );
      assertEquals( context.isTrackingTransactionActive(), true );
    } );

    assertEquals( observer.getName(), name );
    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), true );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertEquals( context.getSpy().asObserverInfo( observer ).isActive(), false );
  }

  @Test
  public void autorun_noDependencies()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();

    Arez.context().observer( callCount::incrementAndGet, Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void schedule_autorun_doesNotExecuteIfNotStale()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger onDepsChangedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsChangedCallCount::incrementAndGet );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangedCallCount.get(), 0 );

    observer.schedule();

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangedCallCount.get(), 0 );
  }

  @Test
  public void reportStale_schedules_autorun()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );

    context.safeAction( observer::reportStale );

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void schedule_onManuallyScheduled_autorun()
    throws Exception
  {
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger onDepsChangedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsChangedCallCount::incrementAndGet, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangedCallCount.get(), 0 );

    context.safeAction( observer::reportStale );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangedCallCount.get(), 1 );

    observer.schedule();

    assertEquals( callCount.get(), 2 );
    assertEquals( onDepsChangedCallCount.get(), 1 );
  }
}
