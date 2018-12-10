package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.Disposable;
import arez.Flags;
import arez.ObservableValue;
import arez.SafeFunction;
import arez.SafeProcedure;
import arez.TestSpyEventHandler;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputableValueApiTest
  extends AbstractArezTest
{
  @Test
  public void reportPossiblyChanged()
  {
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ComputableValue<Integer> computableValue = context.computable( () -> {
      computedCallCount.incrementAndGet();
      return result.get();
    }, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( computedCallCount.get(), 0 );
    assertEquals( observerCallCount.get(), 0 );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computableValue.get();
    } );

    assertEquals( computedCallCount.get(), 1 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computableValue.get(), (Integer) 42 ) );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( computedCallCount.get(), 2 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computableValue.get(), (Integer) 42 ) );

    result.set( 21 );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( computedCallCount.get(), 3 );
    assertEquals( observerCallCount.get(), 2 );

    context.safeAction( () -> assertEquals( computableValue.get(), (Integer) 21 ) );
  }

  @Test
  public void reportPossiblyChanged_onDisposedWithInvariantsDisabled()
  {
    ArezTestUtil.noCheckInvariants();
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ComputableValue<Integer> computableValue = context.computable( () -> {
      computedCallCount.incrementAndGet();
      return result.get();
    } );

    computableValue.dispose();

    assertInvariantFailure( () -> context.safeAction( computableValue::reportPossiblyChanged ),
                            "Arez-0121: The method reportPossiblyChanged() was invoked on disposed ComputableValue named 'ComputableValue@1'." );

    assertEquals( computedCallCount.get(), 0 );
  }

  @Test
  public void computedWithNoDependencies()
  {
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger computedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ComputableValue<Integer> computableValue = context.computable( () -> {
      computedCallCount.incrementAndGet();
      return 1;
    }, Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( computedCallCount.get(), 0 );
    assertEquals( observerCallCount.get(), 0 );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computableValue.get();
    } );

    assertEquals( computedCallCount.get(), 1 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computableValue.get(), (Integer) 1 ) );
  }

  @Test
  public void arezOrExternalDependencies()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final AtomicReference<String> result = new AtomicReference<>();
    result.set( "" );
    final SafeFunction<String> action = () -> {
      calls.incrementAndGet();
      return result.get();
    };
    final ComputableValue<String> computableValue =
      context.computable( "TestComputableValue", action, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | Flags.KEEPALIVE );

    final AtomicInteger observerCallCount = new AtomicInteger();
    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computableValue.get();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( () -> assertEquals( computableValue.get(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( computableValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computableValue.get(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 2 );

    result.set( "NewValue" );

    context.action( computableValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computableValue.get(), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( calls.get(), 3 );
  }

  @Test
  public void keepAlive()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observeADependency();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( action, Flags.KEEPALIVE );

    assertEquals( calls.get(), 1 );

    context.action( computableValue::get );

    assertEquals( calls.get(), 1 );

    context.action( computableValue::get );

    assertEquals( calls.get(), 1 );

    computableValue.dispose();
  }

  @Test
  public void keepAliveViaLock()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = Arez.context().observable();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observable.reportObserved();
      calls.incrementAndGet();
      return "";
    };

    final ComputableValue<String> computableValue = context.computable( "MyComputable", action );
    assertEquals( calls.get(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.action( computableValue::get );
    assertEquals( calls.get(), 1 );

    handler.assertEventCount( 9 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    context.action( computableValue::get );
    assertEquals( calls.get(), 2 );

    handler.assertEventCount( 9 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    final Disposable keepAliveLock = computableValue.keepAlive();

    handler.assertEventCount( 6 );
    handler.assertNextEvent( ObserveScheduleEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );

    handler.reset();

    assertEquals( calls.get(), 3 );

    context.action( computableValue::get );
    assertEquals( calls.get(), 3 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    context.action( computableValue::get );
    assertEquals( calls.get(), 3 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    keepAliveLock.dispose();

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), "MyComputable.deactivate" ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), "MyComputable.deactivate" ) );
    handler.assertNextEvent( TransactionCompleteEvent.class,
                             e -> assertEquals( e.getName(), "MyComputable.deactivate" ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), "MyComputable.deactivate" ) );

    handler.reset();

    keepAliveLock.dispose();

    handler.assertEventCount( 0 );
  }

  @Test
  public void keepAliveViaLockInsideTransactions()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observable = Arez.context().observable();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      observable.reportObserved();
      calls.incrementAndGet();
      return "";
    };

    final ComputableValue<String> computableValue = context.computable( "MyComputable", action );
    assertEquals( calls.get(), 0 );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    context.action( computableValue::get );
    assertEquals( calls.get(), 1 );

    handler.assertEventCount( 9 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    context.action( computableValue::get );
    assertEquals( calls.get(), 2 );

    handler.assertEventCount( 9 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    final Disposable keepAliveLock = context.safeAction( computableValue::keepAlive );

    handler.assertEventCount( 9 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    assertEquals( calls.get(), 3 );

    context.action( computableValue::get );
    assertEquals( calls.get(), 3 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    context.action( computableValue::get );
    assertEquals( calls.get(), 3 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    context.safeAction( "MyAction", (SafeProcedure) keepAliveLock::dispose, Flags.NO_VERIFY_ACTION_REQUIRED );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), "MyAction" ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), "MyAction" ) );
    handler.assertNextEvent( TransactionCompleteEvent.class,
                             e -> assertEquals( e.getName(), "MyAction" ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), "MyAction" ) );

    handler.reset();

    keepAliveLock.dispose();

    handler.assertEventCount( 0 );
  }
}
