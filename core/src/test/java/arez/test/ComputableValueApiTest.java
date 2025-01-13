package arez.test;

import arez.AbstractTest;
import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.ArezTestUtil;
import arez.ComputableValue;
import arez.Disposable;
import arez.ObservableValue;
import arez.Observer;
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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputableValueApiTest
  extends AbstractTest
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
    }, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

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
  public void reportPossiblyChanged_notObserved()
  {
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ComputableValue<Integer> computableValue = context.computable( () -> {
      computedCallCount.incrementAndGet();
      return result.get();
    }, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( computedCallCount.get(), 0 );

    context.safeAction( () -> assertEquals( computableValue.get(), (Integer) 42 ) );

    assertEquals( computedCallCount.get(), 1 );

    result.set( 21 );

    // This MUST not produce an invariant failure because transaction was not used
    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( computedCallCount.get(), 1 );
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
    }, ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES );

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
      context.computable( "TestComputableValue",
                          action,
                          ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | ComputableValue.Flags.KEEPALIVE );

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
      context.computable( action, ComputableValue.Flags.KEEPALIVE );

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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

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

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

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

    context.safeAction( "MyAction", (SafeProcedure) keepAliveLock::dispose, ActionFlags.NO_VERIFY_ACTION_REQUIRED );

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

  @Test
  public void callbackSequencing()
  {
    final ArrayList<String> trace = new ArrayList<>();
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final SafeFunction<Double> function = () -> {
      context.registerOnDeactivateHook( () -> trace.add( "onDeactivate1" ) );
      context.registerOnDeactivateHook( () -> trace.add( "onDeactivate2" ) );
      observable.reportObserved();
      trace.add( "compute" );
      return Math.random();
    };
    final ComputableValue<Double> computable =
      context.computable( null,
                          function,
                          () -> trace.add( "onActivate" ) );

    assertEquals( String.join( " ", trace ), "" );

    final Observer observer = context.observer( computable::get );

    assertEquals( String.join( " ", trace ), "onActivate compute" );

    observer.dispose();

    assertEquals( String.join( " ", trace ), "onActivate compute onDeactivate1 onDeactivate2" );

    computable.dispose();

    assertEquals( String.join( " ", trace ), "onActivate compute onDeactivate1 onDeactivate2" );
  }

  @Test
  public void callbackSequencing_inAction()
  {
    final ArrayList<String> trace = new ArrayList<>();
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final SafeFunction<Double> function = () -> {
      context.registerOnDeactivateHook( () -> trace.add( "onDeactivate1" ) );
      context.registerOnDeactivateHook( () -> trace.add( "onDeactivate2" ) );
      observable.reportObserved();
      trace.add( "compute" );
      return Math.random();
    };
    final ComputableValue<Double> computable =
      context.computable( null,
                          function,
                          () -> trace.add( "onActivate" ) );

    assertEquals( String.join( " ", trace ), "" );

    context.safeAction( computable::get );

    assertEquals( String.join( " ", trace ), "onActivate compute onDeactivate1 onDeactivate2" );

    computable.dispose();

    assertEquals( String.join( " ", trace ), "onActivate compute onDeactivate1 onDeactivate2" );
  }

  @Test
  public void readOutsideTransaction()
  {
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();
    final ComputableValue<Integer> computableValue = context.computable( () -> {
      computedCallCount.incrementAndGet();
      observable.reportObserved();
      return result.get();
    }, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | ComputableValue.Flags.READ_OUTSIDE_TRANSACTION );

    assertEquals( computedCallCount.get(), 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();
    assertEquals( computableValue.get().intValue(), 42 );

    assertEquals( computedCallCount.get(), 1 );

    assertFalse( context.getSpy().asComputableValueInfo( computableValue ).isActive() );

    handler.assertEventCount( 7 );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
  }
}
