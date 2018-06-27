package arez;

import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableChangedEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.toString(), name );
    assertEquals( computedValue.isKeepAlive(), false );

    // Value starts out as null
    assertEquals( computedValue.getValue(), null );

    // Verify the linking of all child elements
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().isDerivation(), true );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
    assertEquals( computedValue.getObserver().getComputedValue(), computedValue );
    assertEquals( computedValue.getObserver().getState(), ObserverState.INACTIVE );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObservable().hasOwner(), true );
    assertEquals( computedValue.getObservable().getOwner(), computedValue.getObserver() );

    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 0 );
  }

  @Test
  public void initialStateOfKeepAlive()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final Observable<Object> observable = Arez.context().createObservable();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observable.reportObserved();
      return "";
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, true );

    computedValue.getObserver().invokeReaction();

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.toString(), name );
    assertEquals( computedValue.isKeepAlive(), true );

    // Value is populated as keepAlive
    assertEquals( computedValue.getValue(), "" );

    // Verify the linking of all child elements
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().isDerivation(), true );
    assertEquals( computedValue.getObserver().getPriority(), Priority.NORMAL );
    assertEquals( computedValue.getObserver().getComputedValue(), computedValue );
    assertEquals( computedValue.getObserver().getState(), ObserverState.UP_TO_DATE );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObservable().hasOwner(), true );
    assertEquals( computedValue.getObservable().getOwner(), computedValue.getObserver() );

    assertEquals( context.getTopLevelComputedValues().get( computedValue.getName() ), computedValue );
    assertEquals( context.getTopLevelObservers().size(), 0 );
    assertEquals( context.getTopLevelObservables().size(), 1 );
  }

  @Test
  public void highestPriorityComputedValue()
    throws Exception
  {
    final ComputedValue<String> computedValue =
      new ComputedValue<>( Arez.context(),
                           null,
                           ValueUtil.randomString(),
                           () -> "",
                           Priority.HIGHEST,
                           false );
    assertEquals( computedValue.getObserver().getPriority(), Priority.HIGHEST );
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
      expectThrows( IllegalStateException.class,
                    () -> new ComputedValue<>( context,
                                               component,
                                               name,
                                               () -> "",
                                               Priority.NORMAL,
                                               false ) );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, component, name, () -> "", Priority.NORMAL, false );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );

    assertEquals( computedValue.getObserver().getComponent(), null );
    assertEquals( computedValue.getObservable().getComponent(), null );

    // Don't register the worker observables/observers just the computed values
    assertEquals( component.getObservables().size(), 0 );
    assertEquals( component.getObservers().size(), 0 );

    assertTrue( component.getComputedValues().contains( computedValue ) );

    computedValue.dispose();

    assertFalse( component.getComputedValues().contains( computedValue ) );
  }

  @Test
  public void computeValue()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final AtomicReference<ComputedValue<String>> ref = new AtomicReference<>();
    value.set( "" );
    final SafeFunction<String> function = () -> {
      observeADependency();
      assertTrue( ref.get().isComputing() );
      return value.get();
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( value1 );

    value.set( value2 );

    setCurrentTransaction( computedValue.getObserver() );
    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void compute_whereLastStateProducedAnError()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( null );
    computedValue.setError( new IllegalStateException() );

    value.set( value2 );

    setCurrentTransaction( computedValue.getObserver() );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void compute_whereValueMatches()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      return value.get();
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    value.set( value1 );
    computedValue.setValue( value1 );

    setCurrentTransaction( computedValue.getObserver() );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void compute_producesException()
    throws Exception
  {
    final ArezContext context = Arez.context();
    final String name = ValueUtil.randomString();
    final String value1 = ValueUtil.randomString();
    final String message = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      observeADependency();
      throw new IllegalStateException( message );
    };
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, name, function, Priority.NORMAL, false );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( value1 );

    setCurrentTransaction( computedValue.getObserver() );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::compute );
    assertEquals( exception.getMessage(), message );

    assertEquals( computedValue.getValue(), null );
    assertEquals( computedValue.getError(), exception );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void compute_noDependencies()
    throws Exception
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, "XYZ", ValueUtil::randomString, Priority.NORMAL, true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> Arez.context().safeAction( computedValue::get ) );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: XYZ Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0173: ComputedValue named 'XYZ' completed compute but is not observing any observables and thus will never be rescheduled. This is not be a ComputedValue candidate." );
    assertEquals( exception.getMessage(),
                  "Arez-0173: ComputedValue named 'XYZ' completed compute but is not observing any observables and thus will never be rescheduled. This is not be a ComputedValue candidate." );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.isDisposed(), false );

    Transaction.setTransaction( null );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void dispose_generates_spyEvent()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.isDisposed(), false );

    Transaction.setTransaction( null );

    final TestSpyEventHandler handler = new TestSpyEventHandler();
    context.getSpy().addSpyEventHandler( handler );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( observer.getState(), ObserverState.INACTIVE );

    handler.assertEventCount( 14 );

    // This is the part that disposes the associated ComputedValue
    final String disposeAction = computedValue.getName() + ".dispose";
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ComputedValueDisposedEvent.class,
                             e -> assertEquals( e.getComputedValue().getName(), computedValue.getName() ) );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( TransactionStartedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
    handler.assertNextEvent( ActionCompletedEvent.class, e -> assertEquals( e.getName(), disposeAction ) );
  }

  @Test
  public void dispose_nestedInReadOnlyTransaction()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Observable<Object> observable = context.createObservable( "Y" );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class,
                    () -> context.action( "X", false, (Procedure) observable::dispose ) );

    assertEquals( exception.getMessage(),
                  "Arez-0119: Attempting to create READ_WRITE transaction named 'Y.dispose' but it is nested in transaction named 'X' with mode READ_ONLY which is not equal to READ_WRITE." );
  }

  @Test
  public void get_upToDateComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_runtimeException()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();
    computedValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    // Get again produces the same exception again
    final IllegalStateException exception2 = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    final Error error = new Error();
    computedValue.setError( error );

    final Error exception = expectThrows( Error.class, computedValue::get );
    assertEquals( exception, error );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );

    // Get again produces the same exception again
    final Error exception2 = expectThrows( Error.class, computedValue::get );
    assertEquals( exception2, error );
  }

  @Test
  public void get_error_and_value()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();

    computedValue.setValue( "" );
    computedValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception.getMessage(),
                  "Arez-0051: ComputedValue generated a value during computation for ComputedValue named '" +
                  computedValue.getName() + "' but still has a non-null value." );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
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
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), function, Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.STALE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_disposedComputedValue()
    throws Exception
  {
    final ArezContext context = Arez.context();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.STALE );
    computedValue.setValue( "XXX" );

    observer.setDisposed( true );

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

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Priority.NORMAL, false );
    final Observer observer = computedValue.getObserver();

    setupReadOnlyTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    computedValue.setValue( "XXX" );

    computedValue.setComputing( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::get );

    assertEquals( exception.getMessage(),
                  "Arez-0049: Detected a cycle deriving ComputedValue named '" + computedValue.getName() + "'." );

    computedValue.setComputing( false );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }
}
