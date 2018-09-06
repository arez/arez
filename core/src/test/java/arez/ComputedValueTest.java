package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueChangedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
public class ComputedValueTest
  extends AbstractArezTest
{
  @Test
  public void initialState()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";
    final Procedure onActivate = new NoopProcedure();
    final Procedure onDeactivate = new NoopProcedure();
    final Procedure onStale = new NoopProcedure();

    final ComputedValue<String> computedValue = context.computed( name, function, onActivate, onDeactivate, onStale );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.toString(), name );
    assertEquals( computedValue.getObserver().isKeepAlive(), false );
    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), false );

    // Value starts out as null
    assertEquals( computedValue.getValue(), null );

    assertEquals( computedValue.getOnActivate(), onActivate );
    assertEquals( computedValue.getOnDeactivate(), onDeactivate );
    assertEquals( computedValue.getOnStale(), onStale );

    // Verify the linking of all child elements
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().isComputedValue(), true );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
    assertEquals( computedValue.getObserver().getComputedValue(), computedValue );
    assertEquals( computedValue.getObserver().getState(), Flags.STATE_INACTIVE );
    assertEquals( computedValue.getObservableValue().getName(), name );
    assertEquals( computedValue.getObservableValue().hasOwner(), true );
    assertEquals( computedValue.getObservableValue().getOwner(), computedValue.getObserver() );

    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 0 );
  }

  @Test
  public void initialStateOfKeepAlive()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final ObservableValue<Object> observableValue = Arez.context().observable();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      return "";
    };
    final ComputedValue<String> computedValue =
      context.computed( name, function, Flags.KEEPALIVE | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    computedValue.getObserver().invokeReaction();

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.toString(), name );
    assertEquals( computedValue.getObserver().isKeepAlive(), true );
    assertEquals( computedValue.getObserver().canObserveLowerPriorityDependencies(), true );

    // Value is populated as keepAlive
    assertEquals( computedValue.getValue(), "" );

    // Verify the linking of all child elements
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().isComputedValue(), true );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
    assertEquals( computedValue.getObserver().getComputedValue(), computedValue );
    assertEquals( computedValue.getObserver().getState(), Flags.STATE_UP_TO_DATE );
    assertEquals( computedValue.getObservableValue().getName(), name );
    assertEquals( computedValue.getObservableValue().hasOwner(), true );
    assertEquals( computedValue.getObservableValue().getOwner(), computedValue.getObserver() );

    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 1 );
  }

  @Test
  public void highPriorityComputedValue()
    throws Exception
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "", Flags.PRIORITY_HIGH );
    assertEquals( computedValue.getObserver().getPriority(), Priority.HIGH );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
    throws Exception
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
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> context.computed( component, name, () -> "" ) );
    assertEquals( exception.getMessage(),
                  "Arez-0048: ComputedValue named '" + name + "' has component specified but " +
                  "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
    throws Exception
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
    final ComputedValue<String> computedValue = context.computed( component, name, () -> "" );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );

    assertEquals( computedValue.getObserver().getComponent(), null );
    assertEquals( computedValue.getObservableValue().getComponent(), null );

    // Don't register the worker observables/observers just the computed values
    assertEquals( component.getObservableValues().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( component.getComputedValues().contains( computedValue ) );

    computedValue.dispose();

    assertFalse( component.getComputedValues().contains( computedValue ) );
  }

  @Test
  public void computedValue_withKeepAliveAndOnActivate()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Procedure action = () -> {
    };

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.computed( null, () -> "", action, null, null, Flags.KEEPALIVE ) );
    assertEquals( exception.getMessage(),
                  "Arez-0039: ArezContext.computed() specified keepAlive = true and did not pass a null for onActivate." );
  }

  @Test
  public void computedValue_withKeepAliveAndOnDeactivate()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Procedure action = () -> {
    };
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.computed( null, () -> "", null, action, null, Flags.KEEPALIVE ) );
    assertEquals( exception.getMessage(),
                  "Arez-0045: ArezContext.computed() specified keepAlive = true and did not pass a null for onDeactivate." );
  }

  @Test
  public void computeValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final AtomicReference<ComputedValue<String>> ref = new AtomicReference<>();
    value.set( "" );
    final SafeFunction<String> function = () -> {
      observeADependency();
      assertTrue( ref.get().isComputing() );
      return value.get();
    };
    final ComputedValue<String> computedValue = context.computed( function );
    ref.set( computedValue );
    setCurrentTransaction( computedValue.getObserver() );

    assertEquals( computedValue.computeValue(), "" );

    value.set( "XXX" );

    assertEquals( computedValue.computeValue(), "XXX" );
  }

  @Test
  public void compute()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputedValue<String> computedValue = context.computed( name, function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );
    setCurrentTransaction( computedValue.getObserver() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );
    computedValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computedValue.getObservableValue() );

    computedValue.getObservableValue().setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    computedValue.setValue( value1 );

    value.set( value2 );

    setCurrentTransaction( computedValue.getObserver() );
    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void compute_whereLastStateProducedAnError()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computedValue.getObserver() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );
    computedValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computedValue.getObservableValue() );

    computedValue.getObservableValue().setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    computedValue.setValue( null );
    computedValue.setError( new IllegalStateException() );

    value.set( value2 );

    setCurrentTransaction( computedValue.getObserver() );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void compute_whereValueMatches()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computedValue.getObserver() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );
    computedValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computedValue.getObservableValue() );

    computedValue.getObservableValue().setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    value.set( value1 );
    computedValue.setValue( value1 );

    setCurrentTransaction( computedValue.getObserver() );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), Flags.STATE_POSSIBLY_STALE );
  }

  @Test
  public void compute_producesException()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String value1 = ValueUtil.randomString();
    final String message = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException( message );
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final Observer observer = context.observer( new CountAndObserveProcedure() );

    setCurrentTransaction( computedValue.getObserver() );

    observer.setState( Flags.STATE_POSSIBLY_STALE );
    computedValue.getObservableValue().rawAddObserver( observer );
    observer.getDependencies().add( computedValue.getObservableValue() );

    computedValue.getObservableValue().setLeastStaleObserverState( Flags.STATE_POSSIBLY_STALE );

    computedValue.setValue( value1 );

    setCurrentTransaction( computedValue.getObserver() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::compute );
    assertEquals( exception.getMessage(), message );

    assertEquals( computedValue.getValue(), null );
    assertEquals( computedValue.getError(), exception );
    assertEquals( observer.getState(), Flags.STATE_STALE );
  }

  @Test
  public void compute_noDependencies()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      context.computed( "XYZ", ValueUtil::randomString, Flags.KEEPALIVE | Flags.DEFER_REACT );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().safeAction( computedValue::get ) );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: XYZ Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0173: ComputedValue named 'XYZ' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputedValue candidate." );
    assertEquals( exception.getMessage(),
                  "Arez-0173: ComputedValue named 'XYZ' completed compute but is not observing any properties. As a result compute will never be rescheduled. This is not a ComputedValue candidate." );
  }

  @Test
  public void compute_noDependencies_but_mustAccessArezState_is_false()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      context.computed( ValueUtil::randomString, Flags.NON_AREZ_DEPENDENCIES );

    assertNotNull( context.safeAction( computedValue::get ) );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.isDisposed(), false );

    Transaction.setTransaction( null );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( observer.getState(), Flags.STATE_DISPOSED );
  }

  @Test
  public void dispose_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( observer );
    observer.setState( Flags.STATE_UP_TO_DATE );

    assertEquals( observer.isDisposed(), false );

    Transaction.setTransaction( null );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( observer.getState(), Flags.STATE_DISPOSED );

    handler.assertEventCount( 14 );

    // This is the part that disposes the associated ComputedValue
    final String disposeAction = computedValue.getName() + ".dispose";
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ComputedValueDisposedEvent.class,
                             e -> assertEquals( e.getComputedValue().getName(), computedValue.getName() ) );

    // This is the part that disposes the associated ObservableValue
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ObservableValueChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
  }

  @Test
  public void dispose_nestedInReadOnlyTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable( "Y" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.action( "X", false, (Procedure) observableValue::dispose ) );

    assertEquals( exception.getMessage(),
                  "Arez-0119: Attempting to create READ_WRITE transaction named 'Y.dispose' but it is nested in transaction named 'X' with mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void get_upToDateComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_UP_TO_DATE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_runtimeException()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();
    computedValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    // Get again produces the same exception again
    final IllegalStateException exception2 = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_UP_TO_DATE );
    final Error error = new Error();
    computedValue.setError( error );

    final Error exception = expectThrows( Error.class, computedValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );

    // Get again produces the same exception again
    final Error exception2 = expectThrows( Error.class, computedValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error_and_value()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();

    computedValue.setValue( "" );
    computedValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception.getMessage(),
                  "Arez-0051: ComputedValue generated a value during computation for ComputedValue named '" +
                  computedValue.getName() + "' but still has a non-null value." );

    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_staleComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( function );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_STALE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "" );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void get_disposedComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( observer );

    observer.setState( Flags.STATE_STALE );
    computedValue.setValue( "XXX" );

    observer.markAsDisposed();

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::get );

    assertEquals( exception.getMessage(),
                  "Arez-0050: ComputedValue named '" + computedValue.getName() + "' accessed after it " +
                  "has been disposed." );
  }

  @Test
  public void get_cycleDetected()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue = context.computed( () -> "" );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( Flags.STATE_UP_TO_DATE );
    computedValue.setValue( "XXX" );

    computedValue.setComputing( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::get );

    assertEquals( exception.getMessage(),
                  "Arez-0049: Detected a cycle deriving ComputedValue named '" + computedValue.getName() + "'." );

    computedValue.setComputing( false );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), Flags.STATE_UP_TO_DATE );
  }

  @Test
  public void getObservable_onDisposedObserver()
    throws Exception
  {
    final ComputedValue<String> computedValue = Arez.context().computed( () -> "" );

    computedValue.setDisposed( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::getObservableValue );

    assertEquals( exception.getMessage(),
                  "Arez-0084: Attempted to invoke getObservableValue on disposed ComputedValue named '" +
                  computedValue.getName() + "'." );

    computedValue.setDisposed( false );

    assertEquals( computedValue.getObservableValue().getName(), computedValue.getName() );
  }

  @Test
  public void reportPossiblyChanged_observerUpToDate()
    throws Throwable
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
    final ComputedValue<String> computedValue = context.computed( function, Flags.NON_AREZ_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    expected.set( "0" );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      assertEquals( computedValue.get(), expected.get() );
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 2 );

    result.set( 23 );
    expected.set( "23" );

    assertEquals( computedCallCount.get(), 2 );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 3 );

    context.safeAction( observableValue::reportChanged );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( computedCallCount.get(), 4 );
  }

  @Test
  public void reportPossiblyChanged_readOnlyTransaction()
    throws Throwable
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
    final ComputedValue<String> computedValue = context.computed( function, Flags.NON_AREZ_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( false, computedValue::reportPossiblyChanged ) );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    assertEquals( exception.getMessage(),
                  "Arez-0152: Transaction named 'Transaction@4' attempted to change ObservableValue named '" +
                  computedValue.getName() + "' but the transaction mode is READ_ONLY." );
  }

  @Test
  public void reportPossiblyChanged_arezOnlyDependencies()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final ObservableValue<Object> observableValue = context.observable();

    final SafeFunction<String> function = () -> {
      observableValue.reportObserved();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.safeAction( false, computedValue::reportPossiblyChanged ) );

    assertEquals( exception.getMessage(),
                  "Arez-0085: The method reportPossiblyChanged() was invoked on ComputedValue named '" +
                  computedValue.getName() + "' but the computed value has arezOnlyDependencies = true." );
  }

  @Test
  public void reportPossiblyChanged_computedAlreadyScheduled()
    throws Throwable
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
    final ComputedValue<String> computedValue = context.computed( function, Flags.NON_AREZ_DEPENDENCIES );

    assertEquals( autorunCallCount.get(), 0 );
    assertEquals( computedCallCount.get(), 0 );

    context.observer( () -> {
      autorunCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( computedCallCount.get(), 1 );

    context.safeAction( () -> {
      observableValue.reportChanged();
      assertTrue( computedValue.getObserver().isScheduled() );
      computedValue.reportPossiblyChanged();
      assertTrue( computedValue.getObserver().isScheduled() );
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
    final ComputedValue<String> computedValue = context.computed( function );

    final ComputedValueInfo info = computedValue.asInfo();
    assertEquals( info.getName(), computedValue.getName() );
  }

  @Test
  public void asInfo_spyDisabled()
  {
    ArezTestUtil.disableSpies();
    ArezTestUtil.resetState();

    final ArezContext context = Arez.context();

    final SafeFunction<String> function = () -> {
      observeADependency();
      return "";
    };
    final ComputedValue<String> computedValue = context.computed( function );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::asInfo );
    assertEquals( exception.getMessage(),
                  "Arez-0195: ComputedValue.asInfo() invoked but Arez.areSpiesEnabled() returned false." );
  }
}
