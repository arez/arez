package arez.test;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.Function;
import arez.ObservableValue;
import arez.Observer;
import arez.Procedure;
import java.util.concurrent.atomic.AtomicInteger;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserverApiTest
  extends AbstractTest
{
  @Test
  public void autorun()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger callCount = new AtomicInteger();

    final String name = ValueUtil.randomString();
    final Observer observer = context.observer( name, () -> {
      observeADependency();
      callCount.incrementAndGet();
      assertTrue( context.isTransactionActive() );
      assertFalse( context.isReadWriteTransactionActive() );
      assertTrue( context.isTrackingTransactionActive() );
      assertFalse( context.isComputableTransactionActive() );
    } );

    assertEquals( observer.getName(), name );
    assertTrue( context.getSpy().asObserverInfo( observer ).isActive() );
    assertEquals( callCount.get(), 1 );

    observer.dispose();

    assertFalse( context.getSpy().asObserverInfo( observer ).isActive() );
  }

  @Test
  public void autorun_noDependencies()
  {
    final AtomicInteger callCount = new AtomicInteger();

    Arez.context().observer( callCount::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );
  }

  @Test
  public void schedule_autorun_doesNotExecuteIfNotStale()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsChangeCallCount::incrementAndGet );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangeCallCount.get(), 0 );

    observer.schedule();

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangeCallCount.get(), 0 );
  }

  @Test
  public void reportStale_schedules_autorun()
  {
    final AtomicInteger callCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, Observer.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );

    context.safeAction( observer::reportStale );

    assertEquals( callCount.get(), 2 );
  }

  @Test
  public void schedule_onManuallyScheduled_autorun()
  {
    final AtomicInteger callCount = new AtomicInteger();
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final Observer observer = context.observer( () -> {
      observable.reportObserved();
      callCount.incrementAndGet();
    }, onDepsChangeCallCount::incrementAndGet, Observer.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangeCallCount.get(), 0 );

    context.safeAction( observer::reportStale );

    assertEquals( callCount.get(), 1 );
    assertEquals( onDepsChangeCallCount.get(), 1 );

    observer.schedule();

    assertEquals( callCount.get(), 2 );
    assertEquals( onDepsChangeCallCount.get(), 1 );
  }

  @Test
  public void application_executed_observer_function_with_no_dependencies_but_AREZ_DEPENDENCIES()
  {
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final Observer observer = context.tracker( onDepsChangeCallCount::incrementAndGet, Observer.Flags.AREZ_DEPENDENCIES );

    final Function<String> observed = ValueUtil::randomString;
    assertInvariantFailure( () -> context.observe( observer, observed ),
                            "Arez-0118: Observer named 'Observer@1' completed observed function (executed by application) but is not observing any properties." );
  }

  @Test
  public void application_executed_observer_function_with_no_dependencies_and_AREZ_OR_NO_DEPENDENCIES()
    throws Throwable
  {
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final Observer observer = context.tracker( onDepsChangeCallCount::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    final Function<String> observed = ValueUtil::randomString;

    // Should generate no exception
    context.observe( observer, observed );
  }

  @Test
  public void application_executed_observer_procedure_with_no_dependencies_but_AREZ_DEPENDENCIES()
  {
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final Observer observer = context.tracker( onDepsChangeCallCount::incrementAndGet, Observer.Flags.AREZ_DEPENDENCIES );

    final Procedure observed = ValueUtil::randomString;
    assertInvariantFailure( () -> context.observe( observer, observed ),
                            "Arez-0118: Observer named 'Observer@1' completed observed function (executed by application) but is not observing any properties." );
  }

  @Test
  public void application_executed_observer_procedure_with_no_dependencies_and_AREZ_OR_NO_DEPENDENCIES()
    throws Throwable
  {
    final AtomicInteger onDepsChangeCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final Observer observer = context.tracker( onDepsChangeCallCount::incrementAndGet, Observer.Flags.AREZ_OR_NO_DEPENDENCIES );

    final Procedure observed = ValueUtil::randomString;

    // Should generate no exception
    context.observe( observer, observed );
  }
}
