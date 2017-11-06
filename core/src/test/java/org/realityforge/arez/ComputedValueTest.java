package org.realityforge.arez;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComputedValueDisposedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;
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
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getContext(), context );
    assertEquals( computedValue.toString(), name );

    // Value starts out as null
    assertEquals( computedValue.getValue(), null );

    // Verify the linking of all child elements
    assertEquals( computedValue.getObserver().getName(), name );
    assertEquals( computedValue.getObserver().isDerivation(), true );
    assertEquals( computedValue.getObserver().getComputedValue(), computedValue );
    assertEquals( computedValue.getObservable().getName(), name );
    assertEquals( computedValue.getObservable().hasOwner(), true );
    assertEquals( computedValue.getObservable().getOwner(), computedValue.getObserver() );
  }

  @Test
  public void constructWithComponentWhenNativeComponentsDisabled()
    throws Exception
  {
    ArezTestUtil.disableNativeComponents();

    final ArezContext context = new ArezContext();
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
                    () -> new ComputedValue<>( context, component, name, () -> "", Objects::equals ) );
    assertEquals( exception.getMessage(),
                  "ComputedValue named '" + name + "' has component specified but " +
                  "Arez.areNativeComponentsEnabled() is false." );
  }

  @Test
  public void basicLifecycle_withComponent()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final Component component =
      new Component( context,
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     ValueUtil.randomString(),
                     null,
                     null );

    final String name = ValueUtil.randomString();
    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, component, name, () -> "", Objects::equals );

    assertEquals( computedValue.getName(), name );
    assertEquals( computedValue.getComponent(), component );

    assertEquals( computedValue.getObserver().getComponent(), null );
    assertEquals( computedValue.getObservable().getComponent(), null );

    assertTrue( component.getComputedValues().contains( computedValue ) );

    computedValue.dispose();

    assertFalse( component.getComputedValues().contains( computedValue ) );
  }

  @Test
  public void computeValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final AtomicReference<ComputedValue<String>> ref = new AtomicReference<>();
    value.set( "" );
    final SafeFunction<String> function = () -> {
      assertTrue( ref.get().isComputing() );
      return value.get();
    };
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );
    ref.set( computedValue );
    assertEquals( computedValue.computeValue(), "" );

    value.set( "XXX" );

    assertEquals( computedValue.computeValue(), "XXX" );
  }

  @Test
  public void compute()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = value::get;
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( value1 );

    value.set( value2 );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void compute_whereLastStateProducedAnError()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value2 = ValueUtil.randomString();
    final SafeFunction<String> function = value::get;
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( null );
    computedValue.setError( new IllegalStateException() );

    value.set( value2 );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value2 );
    assertEquals( computedValue.getError(), null );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void compute_whereValueMatches()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    final String value1 = ValueUtil.randomString();
    final SafeFunction<String> function = value::get;
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    value.set( value1 );
    computedValue.setValue( value1 );

    computedValue.compute();

    assertEquals( computedValue.getValue(), value1 );
    // Verify state does not change
    assertEquals( observer.getState(), ObserverState.POSSIBLY_STALE );
  }

  @Test
  public void compute_producesException()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final String value1 = ValueUtil.randomString();
    final String message = ValueUtil.randomString();
    final SafeFunction<String> function = () -> {
      throw new IllegalStateException( message );
    };
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, null, name, function, comparator );

    setCurrentTransaction( computedValue.getObserver() );

    final Observer observer = newReadOnlyObserver( context );
    observer.setState( ObserverState.POSSIBLY_STALE );
    computedValue.getObserver().getDerivedValue().addObserver( observer );
    observer.getDependencies().add( computedValue.getObservable() );

    computedValue.getObservable().setLeastStaleObserverState( ObserverState.POSSIBLY_STALE );

    computedValue.setValue( value1 );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::compute );
    assertEquals( exception.getMessage(), message );

    assertEquals( computedValue.getValue(), null );
    assertEquals( computedValue.getError(), exception );
    assertEquals( observer.getState(), ObserverState.STALE );
  }

  @Test
  public void dispose()
    throws Exception
  {
    final ArezContext context = new ArezContext();

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
    final ArezContext context = new ArezContext();

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

    handler.assertEventCount( 10 );

    // This is the part that disposes the associated ComputedValue
    final ComputedValueDisposedEvent event = handler.assertNextEvent( ComputedValueDisposedEvent.class );
    assertEquals( event.getComputedValue(), computedValue );

    // This is the part that disposes the Observer
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );

    // This is the part that disposes the associated Observable
    handler.assertNextEvent( ActionStartedEvent.class );
    handler.assertNextEvent( TransactionStartedEvent.class );
    handler.assertNextEvent( ObservableChangedEvent.class );
    handler.assertNextEvent( TransactionCompletedEvent.class );
    handler.assertNextEvent( ActionCompletedEvent.class );
  }

  @Test
  public void dispose_nestedInReadOnlyTransaction()
    throws Throwable
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    context.action( false, () -> {
      observer.setState( ObserverState.UP_TO_DATE );

      assertEquals( observer.isDisposed(), false );

      final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::dispose );

      assertEquals( exception.getMessage(),
                    "Attempting to create READ_WRITE transaction named '" + computedValue.getName() +
                    ".dispose' but it is nested in transaction named '" + context.getTransaction().getName() +
                    "' with mode READ_ONLY which is not equal to READ_WRITE." );

      assertEquals( observer.isDisposed(), false );
    } );
  }

  @Test
  public void get_upToDateComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_runtimeException()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

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
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

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
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    final IllegalStateException error = new IllegalStateException();

    computedValue.setValue( "" );
    computedValue.setError( error );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, computedValue::get );
    assertEquals( exception.getMessage(),
                  "ComputedValue generated a value during computation for ComputedValue named '" +
                  computedValue.getName() + "' but still has a non-null value." );

    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_staleComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

    observer.setState( ObserverState.STALE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_disposedComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( observer );

    observer.setState( ObserverState.STALE );
    computedValue.setValue( "XXX" );

    observer.setDisposed( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::get );

    assertEquals( exception.getMessage(),
                  "ComputedValue named '" + computedValue.getName() + "' accessed after it " +
                  "has been disposed." );
  }

  @Test
  public void get_cycleDetected()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, null, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    computedValue.setValue( "XXX" );

    computedValue.setComputing( true );

    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, computedValue::get );

    assertEquals( exception.getMessage(),
                  "Detected a cycle deriving ComputedValue named '" + computedValue.getName() + "'." );

    computedValue.setComputing( false );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }
}
