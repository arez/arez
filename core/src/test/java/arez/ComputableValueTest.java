package arez;

import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputableValueInfo;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.Priority;
import arez.spy.ReactionCycleCompleteEvent;
import arez.spy.ReactionCycleStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ComputableValueTest
  extends AbstractTest
{
  @Test
  public void initialState()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";

    final ComputableValue<String> computableValue = context.computable( name, function );

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getContext(), context );
    assertEquals( computableValue.toString(), name );
    assertFalse( computableValue.getObserver().isKeepAlive() );
    assertFalse( computableValue.getObserver().canObserveLowerPriorityDependencies() );

    // Value starts out as null
    assertNull( computableValue.getValue() );

    // Verify the linking of all child elements
    assertEquals( computableValue.getObserver().getName(), name );
    assertTrue( computableValue.getObserver().isComputableValue() );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.NORMAL );
    assertEquals( computableValue.getObserver().getComputableValue(), computableValue );
    assertEquals( computableValue.getObserver().getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( computableValue.getObservableValue().getName(), name );
    assertTrue( computableValue.getObservableValue().isComputableValue() );
    assertEquals( computableValue.getObservableValue().getObserver(), computableValue.getObserver() );

    assertEquals( context.getTopLevelComputableValues().get( computableValue.getName() ), computableValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 0 );
  }

  @Test
  public void initialStateOfKeepAlive()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = Arez.context().observable();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( name,
                          function,
                          ComputableValue.Flags.KEEPALIVE | ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    computableValue.getObserver().invokeReaction();

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getContext(), context );
    assertEquals( computableValue.toString(), name );
    assertTrue( computableValue.getObserver().isKeepAlive() );
    assertTrue( computableValue.getObserver().canObserveLowerPriorityDependencies() );

    // Value is populated as keepAlive
    assertEquals( computableValue.getValue(), "" );

    // Verify the linking of all child elements
    assertEquals( computableValue.getObserver().getName(), name );
    assertTrue( computableValue.getObserver().isComputableValue() );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.NORMAL );
    assertEquals( computableValue.getObserver().getComputableValue(), computableValue );
    assertEquals( computableValue.getObserver().getState(), Observer.Flags.STATE_UP_TO_DATE );
    assertEquals( computableValue.getObservableValue().getName(), name );
    assertTrue( computableValue.getObservableValue().isComputableValue() );
    assertEquals( computableValue.getObservableValue().getObserver(), computableValue.getObserver() );

    assertEquals( context.getTopLevelComputableValues().get( computableValue.getName() ), computableValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 1 );
  }

  @Test
  public void highPriorityComputableValue()
  {
    final ComputableValue<String> computableValue =
      Arez.context().computable( () -> "", ComputableValue.Flags.PRIORITY_HIGH );
    assertEquals( computableValue.getObserver().getTask().getPriority(), Priority.HIGH );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = Arez.context();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    assertInvariantFailure( () -> context.computable( component, name, () -> "" ),
                            "Arez-0048: ComputableValue named '" + name + "' has component specified but " +
                            "Arez.areNativeComponentsEnabled() is false." );

  }

  @Test
  public void basicLifecycle_withComponent()
  {
    final ArezContext context = Arez.context();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final ComputableValue<String> computableValue = context.computable( component, name, () -> "" );

    assertEquals( computableValue.getName(), name );
    assertEquals( computableValue.getComponent(), component );

    assertNull( computableValue.getObserver().getComponent() );
    assertNull( computableValue.getObservableValue().getComponent() );

    // Don't register the worker observables/observers just the computable values
    assertEquals( component.getObservableValues().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( component.getComputableValues().contains( computableValue ) );

    computableValue.dispose();

    assertFalse( component.getComputableValues().contains( computableValue ) );
  }

  @Test
  public void computeValue()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final AtomicReference<ComputableValue<String>> ref = new AtomicReference<>();
    value.set( "" );
    final SafeFunction<String> function = () -> {
      observeADependency();
      assertTrue( ref.get().isComputing() );
      return value.get();
    };
    final ComputableValue<String> computableValue = context.computable( function );
    ref.set( computableValue );
    setCurrentTransaction( computableValue.getObserver() );

    assertEquals( computableValue.computeValue(), "" );

    value.set( "XXX" );

    assertEquals( computableValue.computeValue(), "XXX" );
  }

  @Test
  public void compute()
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final String value2 = ValueUtil.randomString();
    final NoopProcedure onDeactivateHook1 = new NoopProcedure();
    final NoopProcedure onDeactivateHook2 = new NoopProcedure();
    final SafeFunction<String> function = () -> {
      observeADependency();
      Arez.context().registerHook( "1", null, onDeactivateHook1 );
      Arez.context().registerHook( "2", null, onDeactivateHook2 );
      return value.get();
    };
    final ComputableValue<String> computableValue = context.computable( name, function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    computableValue.setValue( value1 );

    value.set( value2 );

    setCurrentTransaction( computableValue.getObserver() );

    assertNull( Transaction.current().getHooks() );
    assertEquals( computableValue.getObserver().getHooks().size(), 0 );

    computableValue.compute();

    assertEquals( computableValue.getValue(), value2 );
    assertNull( computableValue.getError() );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );

    // The hooks should have been registered with the transaction
    final HookMap hooks = Transaction.current().getHooks();
    assertNotNull( hooks );
    assertEquals( hooks.size(), 2 );
    assertTrue( containsOnDeactivateHook( hooks, onDeactivateHook1 ) );
    assertTrue( containsOnDeactivateHook( hooks, onDeactivateHook2 ) );

    // The hooks will not be updated until transaction completes
    assertEquals( computableValue.getObserver().getHooks().size(), 0 );
  }

  @Test
  public void compute_whereLastStateProducedAnError()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    computableValue.setValue( null );
    computableValue.setError( new IllegalStateException() );

    value.set( value2 );

    setCurrentTransaction( computableValue.getObserver() );

    computableValue.compute();

    assertEquals( computableValue.getValue(), value2 );
    assertNull( computableValue.getError() );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
  }

  private static boolean containsOnDeactivateHook( final HookMap hooks, final Procedure hook )
  {
    for ( int i = 0; i < hooks.size(); i++ )
    {
      if ( hooks.valueAt( i ).getOnDeactivate() == hook )
      {
        return true;
      }
    }
    return false;
  }

  @Test
  public void compute_whereValueMatches()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    value.set( value1 );
    computableValue.setValue( value1 );

    setCurrentTransaction( computableValue.getObserver() );

    computableValue.compute();

    assertEquals( computableValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void compute_whereValueDeepMatches_withComparator()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<int[]> value = new AtomicReference<>();
    final int[] value1 = new int[]{ 1, 2, 3 };
    final SafeFunction<int[]> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputableValue<int[]> computableValue =
      context.computable( null, null, function, 0, new ObjectsDeepEqualsComparator() );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    value.set( new int[]{ 1, 2, 3 } );
    computableValue.setValue( value1 );

    setCurrentTransaction( computableValue.getObserver() );

    computableValue.compute();

    assertSame( computableValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void compute_whereValueMatches_customComparator()
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputableValue<String> computableValue =
      context.computable( null, null, function, 0, ( oldValue, newValue ) -> true );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    value.set( ValueUtil.randomString() );
    computableValue.setValue( value1 );

    setCurrentTransaction( computableValue.getObserver() );

    computableValue.compute();

    assertEquals( computableValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), Observer.Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void compute_producesException()
  {
    final ArezContext context = Arez.context();
    final String value1 = ValueUtil.randomString();
    final String message = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException( message );
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computableValue.getObserver() );

    observer.setState( Observer.Flags.STATE_POSSIBLY_STALE );
    computableValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computableValue.getObservableValue() );

    computableValue.getObservableValue().setLeastStaleObserverState( Observer.Flags.STATE_POSSIBLY_STALE );

    computableValue.setValue( value1 );

    setCurrentTransaction( computableValue.getObserver() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computableValue::compute );
    assertEquals( exception.getMessage(), message );

    assertNull( computableValue.getValue() );
    assertEquals( computableValue.getError(), exception );
    assertEquals( observer.getState(), Observer.Flags.STATE_STALE );
  }

  @Test
  public void compute_noDependencies()
  {
    ignoreObserverErrors();
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue =
      context.computable( "XYZ",
                          ValueUtil::randomString,
                          ComputableValue.Flags.KEEPALIVE | ComputableValue.Flags.RUN_LATER );

    assertInvariantFailure( () -> Arez.context().safeAction( computableValue::get ),
                            "Arez-0173: ComputableValue named 'XYZ' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputableValue candidate." );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: XYZ Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0173: ComputableValue named 'XYZ' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputableValue candidate." );
  }

  @Test
  public void compute_noDependencies_but_mustAccessArezState_is_false()
  {
    ignoreObserverErrors();
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue =
      context.computable( ValueUtil::randomString, ComputableValue.Flags.AREZ_OR_NO_DEPENDENCIES );

    assertNotNull( context.safeAction( computableValue::get ) );
  }

  @Test
  public void dispose()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setCurrentTransaction( observer );
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );

    assertFalse( observer.isDisposed() );

    Transaction.setTransaction( null );

    computableValue.dispose();

    assertTrue( observer.isDisposed() );
    assertEquals( observer.getState(), Observer.Flags.STATE_DISPOSED );
  }

  @Test
  public void dispose_generates_spyEvent()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setCurrentTransaction( observer );
    observer.setState( Observer.Flags.STATE_UP_TO_DATE );

    assertFalse( observer.isDisposed() );

    Transaction.setTransaction( null );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    computableValue.dispose();

    assertTrue( observer.isDisposed() );
    assertEquals( observer.getState(), Observer.Flags.STATE_DISPOSED );

    handler.assertEventCount( 14 );

    // This is the part that disposes the associated ComputableValue
    final String disposeAction = computableValue.getName() + ".dispose";
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ComputableValueDisposeEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computableValue.getName() ) );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompleteEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
  }

  @Test
  public void dispose_nestedInReadOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable( "Y" );

    assertInvariantFailure( () -> context.action( "X", observableValue::dispose, ActionFlags.READ_ONLY ),
                            "Arez-0119: Attempting to create READ_WRITE transaction named 'Y.dispose' but it is nested in transaction named 'X' with mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void get_upToDateComputableValue()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    computableValue.setValue( "XXX" );

    assertEquals( computableValue.get(), "XXX" );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_runtimeException()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();
    computableValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computableValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    // Get again produces the same exception again
    final IllegalStateException exception2 = expectThrows( IllegalStateException.class, computableValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    final Error error = new Error();
    computableValue.setError( error );

    final Error exception = expectThrows( Error.class, computableValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );

    // Get again produces the same exception again
    final Error exception2 = expectThrows( Error.class, computableValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error_and_value()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();

    computableValue.setValue( "" );
    computableValue.setError( error );

    assertInvariantFailure( computableValue::get,
                            "Arez-0051: ComputableValue generated a value during computation for ComputableValue named '" +
                            computableValue.getName() + "' but still has a non-null value." );

    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_staleComputableValue()
  {
    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_STALE );
    computableValue.setValue( "XXX" );

    assertEquals( computableValue.get(), "" );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_disposedComputableValue()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setCurrentTransaction( observer );

    observer.setState( Observer.Flags.STATE_STALE );
    computableValue.setValue( "XXX" );

    observer.markAsDisposed();

    assertInvariantFailure( computableValue::get,
                            "Arez-0050: ComputableValue named '" + computableValue.getName() + "' accessed after it " +
                            "has been disposed." );
  }

  @Test
  public void get_cycleDetected()
  {
    final ArezContext context = Arez.context();

    final ComputableValue<String> computableValue = context.computable( () -> "" );
    final Observer observer = computableValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Observer.Flags.STATE_UP_TO_DATE );
    computableValue.setValue( "XXX" );

    computableValue.setComputing( true );

    assertInvariantFailure( computableValue::get,
                            "Arez-0049: Detected a cycle deriving ComputableValue named '" +
                            computableValue.getName() +
                            "'." );

    computableValue.setComputing( false );

    assertEquals( computableValue.get(), "XXX" );
    assertEquals( observer.getState(), Observer.Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void getObservable_onDisposedObserver()
  {
    final ComputableValue<String> computableValue = Arez.context().computable( () -> "" );

    computableValue.setDisposed( true );

    assertInvariantFailure( computableValue::getObservableValue,
                            "Arez-0084: Attempted to invoke getObservableValue on disposed ComputableValue named '" +
                            computableValue.getName() + "'." );

    computableValue.setDisposed( false );

    assertEquals( computableValue.getObservableValue().getName(), computableValue.getName() );
  }

  @Test
  public void reportPossiblyChanged_observerUpToDate()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger autorunCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();
    final AtomicReference<String> expected = new AtomicReference<>();

    final ObservableValue<Object> observableValue = context.observable();

    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      computedCallCount.incrementAndGet();
      return String.valueOf( result.get() );
    };
    final ComputableValue<String> computableValue =
      context.computable( function, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    expected.set( "0" );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      assertEquals( computableValue.get(), expected.get() );
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 2 );

    result.set( 23 );
    expected.set( "23" );

    assertEquals( computedCallCount.get(), 2 );

    context.safeAction( computableValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 3 );

    context.safeAction( observableValue::reportChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 4 );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger autorunCallCount = new AtomicInteger();

    final ObservableValue<Object> observableValue = context.observable();

    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      computedCallCount.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( function, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      computableValue.get();
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    assertInvariantFailure( () -> context.safeAction( computableValue::reportPossiblyChanged, ActionFlags.READ_ONLY ),
                            "Arez-0152: Transaction named 'Action@4' attempted to change ObservableValue named '" +
                            computableValue.getName() + "' but the transaction mode is READ_ONLY." );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );
  }

  @Test
  public void reportPossiblyChanged_arezOnlyDependencies()
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable();

    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertInvariantFailure( () -> context.safeAction( computableValue::reportPossiblyChanged ),
                            "Arez-0085: The method reportPossiblyChanged() was invoked on ComputableValue named '" +
                            computableValue.getName() + "' but the computable value has not specified the " +
                            "AREZ_OR_EXTERNAL_DEPENDENCIES flag." );
  }

  @Test
  public void reportPossiblyChanged_computedAlreadyScheduled()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger autorunCallCount = new AtomicInteger();

    final ObservableValue<Object> observableValue = context.observable();

    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      computedCallCount.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue =
      context.computable( function, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      computableValue.get();
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( () -> {
      observableValue.reportChanged();
      assertTrue( computableValue.getObserver().getTask().isQueued() );
      computableValue.reportPossiblyChanged();
      assertTrue( computableValue.getObserver().getTask().isQueued() );
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 2 );
  }

  @Test
  public void asInfo()
  {
    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final ComputableValueInfo info = computableValue.asInfo();
    assertEquals( info.getName(), computableValue.getName() );
  }

  @Test
  public void asInfo_spyDisabled()
  {
    ArezTestUtil.disableSpies();

    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertInvariantFailure( computableValue::asInfo,
                            "Arez-0195: ComputableValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }

  @Test
  public void incrementKeepAliveRefCount()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    assertEquals( calls.get(), 0 );

    computableValue.incrementKeepAliveRefCount();

    assertEquals( calls.get(), 1 );
    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 1 );

    handler.assertEventCount( 8 );
    handler.assertNextEvent( ObserveScheduleEvent.class );
    handler.assertNextEvent( ReactionCycleStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( ReactionCycleCompleteEvent.class );

    handler.reset();

    computableValue.incrementKeepAliveRefCount();

    handler.assertEventCount( 0 );
    assertEquals( calls.get(), 1 );
    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 2 );
  }

  @Test
  public void incrementKeepAliveRefCount_badInitialRefCount()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    final int keepAliveRefCount = -Math.abs( ValueUtil.randomInt() );
    computableValue.setKeepAliveRefCount( keepAliveRefCount );

    assertInvariantFailure( computableValue::incrementKeepAliveRefCount,
                            "Arez-0165: KeepAliveRefCount on ComputableValue named 'ComputableValue@2' " +
                            "has an invalid value " + keepAliveRefCount );
  }

  @Test
  public void decrementKeepAliveRefCount()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );

    assertEquals( calls.get(), 0 );

    computableValue.incrementKeepAliveRefCount();

    assertEquals( calls.get(), 1 );
    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 1 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    computableValue.decrementKeepAliveRefCount();

    assertEquals( calls.get(), 1 );
    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );
  }

  @Test
  public void decrementKeepAliveRefCount_badInitialRefCount()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertInvariantFailure( computableValue::decrementKeepAliveRefCount,
                            "Arez-0165: KeepAliveRefCount on ComputableValue named 'ComputableValue@2' has an invalid value -1" );
  }

  @Test
  public void keepAlive()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      calls.incrementAndGet();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function );

    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );

    assertEquals( calls.get(), 0 );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    final Disposable keepAliveLock1 = computableValue.keepAlive();

    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 1 );

    assertEquals( calls.get(), 1 );

    handler.assertEventCount( 8 );
    handler.assertNextEvent( ObserveScheduleEvent.class );
    handler.assertNextEvent( ReactionCycleStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObservableValueChangeEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ComputeCompleteEvent.class );
    handler.assertNextEvent( ReactionCycleCompleteEvent.class );

    handler.reset();

    final Disposable keepAliveLock2 = computableValue.keepAlive();

    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 2 );
    assertEquals( calls.get(), 1 );
    assertFalse( keepAliveLock1.isDisposed() );
    assertFalse( keepAliveLock2.isDisposed() );
    handler.assertEventCount( 0 );

    keepAliveLock1.dispose();

    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 1 );
    assertEquals( calls.get(), 1 );
    assertTrue( keepAliveLock1.isDisposed() );
    assertFalse( keepAliveLock2.isDisposed() );
    handler.assertEventCount( 0 );

    // Should be a no-op
    keepAliveLock1.dispose();

    assertTrue( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 1 );
    assertEquals( calls.get(), 1 );
    assertTrue( keepAliveLock1.isDisposed() );
    assertFalse( keepAliveLock2.isDisposed() );
    handler.assertEventCount( 0 );

    keepAliveLock2.dispose();

    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );
    assertTrue( keepAliveLock1.isDisposed() );
    assertTrue( keepAliveLock2.isDisposed() );
    assertEquals( calls.get(), 1 );

    handler.assertEventCount( 4 );
    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.reset();

    // Another no-op
    keepAliveLock2.dispose();

    assertFalse( computableValue.getObserver().isActive() );
    assertEquals( computableValue.getKeepAliveRefCount(), 0 );
    assertTrue( keepAliveLock1.isDisposed() );
    assertTrue( keepAliveLock2.isDisposed() );
    assertEquals( calls.get(), 1 );

    handler.assertEventCount( 0 );
  }

  @Test
  public void keepAlive_on_KEEPALIVE_Computable()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observable = context.observable();

    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      return "";
    };
    final ComputableValue<String> computableValue = context.computable( function, ComputableValue.Flags.KEEPALIVE );

    assertInvariantFailure( computableValue::keepAlive,
                            "Arez-0223: ComputableValue.keepAlive() was invoked on computable value named 'ComputableValue@2' but invoking this method when the computable value has been configured with the KEEPALIVE flag is invalid as the computable is always activated." );
  }

  @Test
  public void lazyValidationDoesNotTrackTransitiveComputableDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> upstream = context.computable( middle::get );
    final ComputableValue<Integer> downstream = context.computable( upstream::get );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 1 ) );

    assertDependencies( downstream, upstream.getObservableValue() );
    assertDependencies( upstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    context.safeAction( source::reportChanged );

    assertDependencies( downstream, upstream.getObservableValue() );
    assertDependencies( upstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( middle, downstream );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationSuppressionDoesNotHideLaterDirectReadInSameTransaction()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> upstream = context.computable( middle::get );
    final ComputableValue<Integer> downstream = context.computable( () -> upstream.get() + leaf.get() );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 2 ) );

    assertDependencies( downstream, upstream.getObservableValue(), leaf.getObservableValue() );
    assertDependencies( upstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    context.safeAction( source::reportChanged );

    assertDependencies( downstream, upstream.getObservableValue(), leaf.getObservableValue() );
    assertDependencies( upstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( middle, downstream );
  }

  @Test
  public void lazyValidationChangeStillPropagatesThroughImmediateEdges()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );
    final AtomicInteger expectedValue = new AtomicInteger( 1 );
    final AtomicInteger observerCallCount = new AtomicInteger();

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> upstream = context.computable( middle::get );
    final ComputableValue<Integer> downstream = context.computable( upstream::get );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      assertEquals( downstream.get(), (Integer) expectedValue.get() );
    } );

    sourceValue.set( 2 );
    expectedValue.set( 2 );
    context.safeAction( source::reportChanged );

    assertEquals( observerCallCount.get(), 2 );
    assertDependencies( downstream, upstream.getObservableValue() );
    assertDependencies( upstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( middle, downstream );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationEqualityComparatorNoChangeDoesNotTrackTransitiveComputableDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );
    final AtomicInteger observerCallCount = new AtomicInteger();
    final EqualityComparator comparator =
      ( oldValue, newValue ) -> null != oldValue && null != newValue && (int) oldValue % 2 == (int) newValue % 2;

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle = context.computable( null, null, leaf::get, 0, comparator );
    final ComputableValue<Integer> downstream = context.computable( middle::get );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      assertEquals( downstream.get(), (Integer) 1 );
    } );

    sourceValue.set( 3 );
    context.safeAction( source::reportChanged );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( middle.getValue(), (Integer) 1 );
    assertEquals( leaf.getValue(), (Integer) 3 );
    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationStopsAfterFirstChangedDependencyWithoutTrackingTransitiveComputableDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source1 = context.observable();
    final ObservableValue<Object> source2 = context.observable();
    final AtomicInteger source1Value = new AtomicInteger( 1 );
    final AtomicInteger source2Value = new AtomicInteger( 1 );
    final AtomicInteger secondComputeCallCount = new AtomicInteger();

    final ComputableValue<Integer> first = context.computable( () -> {
      source1.reportObserved();
      return source1Value.get();
    } );
    final ComputableValue<Integer> second = context.computable( () -> {
      secondComputeCallCount.incrementAndGet();
      source2.reportObserved();
      return source2Value.get();
    } );
    final ComputableValue<Integer> downstream = context.computable( () -> first.get() + second.get() );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 2 ) );

    assertEquals( secondComputeCallCount.get(), 1 );
    assertDependencies( downstream, first.getObservableValue(), second.getObservableValue() );
    assertDependencies( first, source1 );
    assertDependencies( second, source2 );

    final SchedulerLock schedulerLock = context.pauseScheduler();
    try
    {
      source1Value.set( 2 );
      source2Value.set( 2 );
      context.safeAction( () -> {
        source1.reportChanged();
        source2.reportChanged();
      } );

      assertEquals( secondComputeCallCount.get(), 1 );

      setCurrentTransaction( downstream.getObserver() );
      try
      {
        assertTrue( downstream.getObserver().shouldCompute() );
      }
      finally
      {
        Transaction.setTransaction( null );
      }

      assertEquals( secondComputeCallCount.get(), 1 );
      assertEquals( downstream.getObserver().getState(), Observer.Flags.STATE_STALE );
      assertDependencies( downstream, first.getObservableValue(), second.getObservableValue() );
      assertDependencies( first, source1 );
      assertDependencies( second, source2 );
      assertFalse( source1.getObservers().contains( downstream.getObserver() ) );
      assertFalse( source2.getObservers().contains( downstream.getObserver() ) );
    }
    finally
    {
      schedulerLock.dispose();
    }
  }

  @Test
  public void lazyValidationRefreshesDynamicDependenciesWithoutTrackingThemOnOuterComputable()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source1 = context.observable();
    final ObservableValue<Object> source2 = context.observable();
    final AtomicBoolean useSource1 = new AtomicBoolean( true );

    final ComputableValue<Integer> leaf = context.computable( () -> {
      if ( useSource1.get() )
      {
        source1.reportObserved();
      }
      else
      {
        source2.reportObserved();
      }
      return 1;
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( middle::get );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 1 ) );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source1 );

    useSource1.set( false );
    context.safeAction( source1::reportChanged );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source2 );
    assertFalse( source1.getObservers().contains( leaf.getObserver() ) );
    assertFalse( source2.getObservers().contains( downstream.getObserver() ) );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationSuppressesReadOutsideTransactionDependencyObservation()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );

    final ComputableValue<Integer> leaf =
      context.computable( () -> {
                            source.reportObserved();
                            return sourceValue.get();
                          },
                          ComputableValue.Flags.READ_OUTSIDE_TRANSACTION );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( middle::get );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 1 ) );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    context.safeAction( source::reportChanged );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationExceptionDoesNotTrackTransitiveComputableDependencies()
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicBoolean throwException = new AtomicBoolean();
    final AtomicInteger observerCallCount = new AtomicInteger();

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      if ( throwException.get() )
      {
        throw new IllegalStateException();
      }
      return 1;
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( middle::get );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      if ( throwException.get() )
      {
        expectThrows( IllegalStateException.class, downstream::get );
      }
      else
      {
        assertEquals( downstream.get(), (Integer) 1 );
      }
    } );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    throwException.set( true );
    context.safeAction( source::reportChanged );

    assertEquals( observerCallCount.get(), 2 );
    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationCachedExceptionDoesNotTrackTransitiveComputableDependencies()
  {
    ignoreObserverErrors();

    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger observerCallCount = new AtomicInteger();

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      throw new IllegalStateException();
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( middle::get );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      expectThrows( IllegalStateException.class, downstream::get );
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    context.safeAction( source::reportChanged );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationDoesNotMoveDependencyHooksToOuterComputable()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );
    final TestProcedure leafOnActivate = new TestProcedure();
    final TestProcedure leafOnDeactivate = new TestProcedure();
    final TestProcedure downstreamOnActivate = new TestProcedure();
    final TestProcedure downstreamOnDeactivate = new TestProcedure();

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      context.registerHook( "leaf", leafOnActivate, leafOnDeactivate );
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( () -> {
      context.registerHook( "downstream", downstreamOnActivate, downstreamOnDeactivate );
      return middle.get();
    } );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 1 ) );

    assertEquals( leafOnActivate.getCalls(), 1 );
    assertEquals( downstreamOnActivate.getCalls(), 1 );
    assertTrue( containsOnDeactivateHook( leaf.getObserver().getHooks(), leafOnDeactivate ) );
    assertTrue( containsOnDeactivateHook( downstream.getObserver().getHooks(), downstreamOnDeactivate ) );

    context.safeAction( source::reportChanged );

    assertEquals( leafOnActivate.getCalls(), 1 );
    assertEquals( downstreamOnActivate.getCalls(), 1 );
    assertTrue( containsOnDeactivateHook( leaf.getObserver().getHooks(), leafOnDeactivate ) );
    assertTrue( containsOnDeactivateHook( downstream.getObserver().getHooks(), downstreamOnDeactivate ) );
    assertFalse( containsOnDeactivateHook( downstream.getObserver().getHooks(), leafOnDeactivate ) );
    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationWithLowerPriorityDependencyDoesNotTrackTransitiveComputableDependencies()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final AtomicInteger sourceValue = new AtomicInteger( 1 );

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return sourceValue.get();
    } );
    final ComputableValue<Integer> middle =
      context.computable( null,
                          null,
                          leaf::get,
                          ComputableValue.Flags.PRIORITY_HIGH |
                          ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    final ComputableValue<Integer> downstream =
      context.computable( null, null, middle::get, ComputableValue.Flags.PRIORITY_HIGH );

    context.observer( () -> assertEquals( downstream.get(), (Integer) 1 ) );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    context.safeAction( source::reportChanged );

    assertDependencies( downstream, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void lazyValidationDoesNotKeepTransitiveComputableDependenciesActiveAfterDeactivation()
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> source = context.observable();
    final ObservableValue<Object> branch = context.observable();
    final AtomicBoolean useMiddle = new AtomicBoolean( true );

    final ComputableValue<Integer> leaf = context.computable( () -> {
      source.reportObserved();
      return 1;
    } );
    final ComputableValue<Integer> middle = context.computable( leaf::get );
    final ComputableValue<Integer> downstream = context.computable( () -> {
      branch.reportObserved();
      return useMiddle.get() ? middle.get() : 0;
    } );

    context.observer( () -> assertEquals( downstream.get(), (Integer) ( useMiddle.get() ? 1 : 0 ) ) );

    assertDependencies( downstream, branch, middle.getObservableValue() );
    assertDependencies( middle, leaf.getObservableValue() );
    assertDependencies( leaf, source );

    useMiddle.set( false );
    context.safeAction( branch::reportChanged );

    assertDependencies( downstream, branch );
    assertEquals( middle.getObserver().getState(), Observer.Flags.STATE_INACTIVE );
    assertEquals( leaf.getObserver().getState(), Observer.Flags.STATE_INACTIVE );
    assertFalse( middle.getObservableValue().getObservers().contains( downstream.getObserver() ) );
    assertNotObserver( leaf, downstream );
  }

  @Test
  public void leastStaleObserverStateMaintainedCorrectly()
  {
    final ArezContext context = Arez.context();

    final AtomicInteger value = new AtomicInteger();
    value.set( 1 );
    final ComputableValue<Integer> computable1 =
      context.computable( value::get, ComputableValue.Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    final ComputableValue<Integer> computable2 =
      context.computable( () -> {
                            computable1.get();
                            return 22;
                          },
                          ComputableValue.Flags.PRIORITY_HIGH |
                          ComputableValue.Flags.KEEPALIVE |
                          ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    final TestSpyEventHandler handler = TestSpyEventHandler.subscribe();

    value.set( 2 );

    context.safeAction( computable1::reportPossiblyChanged );
    handler.assertEventCount( 19 );

    handler.assertNextEvent( ActionStartEvent.class );
    handler.assertNextEvent( TransactionStartEvent.class );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), computable2.getName() ) );
    handler.assertNextEvent( ObserveScheduleEvent.class,
                             e -> assertEquals( e.getObserver().getName(), computable1.getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class );
    handler.assertNextEvent( ActionCompleteEvent.class );

    handler.assertNextEvent( ReactionCycleStartEvent.class );
    handler.assertNextEvent( ComputeStartEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable2.getName() ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), computable2.getName() ) );
    handler.assertNextEvent( ComputeStartEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable1.getName() ) );
    handler.assertNextEvent( TransactionStartEvent.class, e -> assertEquals( e.getName(), computable1.getName() ) );
    handler.assertNextEvent( ObservableValueChangeEvent.class,
                             e -> assertEquals( e.getObservableValue().getName(), computable1.getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), computable1.getName() ) );
    handler.assertNextEvent( ComputeCompleteEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable1.getName() ) );
    handler.assertNextEvent( TransactionCompleteEvent.class, e -> assertEquals( e.getName(), computable2.getName() ) );
    handler.assertNextEvent( ComputeCompleteEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable2.getName() ) );
    handler.assertNextEvent( ComputeStartEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable1.getName() ) );
    handler.assertNextEvent( ComputeCompleteEvent.class,
                             e -> assertEquals( e.getComputableValue().getName(), computable1.getName() ) );
    handler.assertNextEvent( ReactionCycleCompleteEvent.class );
  }

  private static void assertDependencies( final ComputableValue<?> computableValue,
                                          final ObservableValue<?>... dependencies )
  {
    final FastList<ObservableValue<?>> actual = computableValue.getObserver().getDependencies();
    assertEquals( actual.size(), dependencies.length );
    for ( int i = 0; i < dependencies.length; i++ )
    {
      assertSame( actual.get( i ), dependencies[ i ] );
      assertTrue( dependencies[ i ].getObservers().contains( computableValue.getObserver() ) );
    }
  }

  private static void assertNotObserver( final ComputableValue<?> observable,
                                         final ComputableValue<?> observer )
  {
    assertFalse( observable.getObservableValue().getObservers().contains( observer.getObserver() ) );
    assertFalse( observer.getObserver().getDependencies().contains( observable.getObservableValue() ) );
  }
}
