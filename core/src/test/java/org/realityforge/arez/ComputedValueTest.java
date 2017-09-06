package org.realityforge.arez;

import java.util.Objects;
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
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final SafeFunction<String> function = () -> "";
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, name, function, comparator );

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
    assertEquals( computedValue.getObservable().isCalculated(), true );
    assertEquals( computedValue.getObservable().getOwner(), computedValue.getObserver() );
  }

  @Test
  public void computeValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();
    final String name = ValueUtil.randomString();
    final AtomicReference<String> value = new AtomicReference<>();
    value.set( "" );
    final SafeFunction<String> function = value::get;
    final EqualityComparator<String> comparator = Objects::equals;
    final ComputedValue<String> computedValue = new ComputedValue<>( context, name, function, comparator );

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
    final ComputedValue<String> computedValue = new ComputedValue<>( context, name, function, comparator );

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
    final ComputedValue<String> computedValue = new ComputedValue<>( context, name, function, comparator );

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
  public void dispose()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final Observer observer = newDerivation( context );
    final ComputedValue<?> computedValue = observer.getComputedValue();

    setCurrentTransaction( observer );
    observer.setState( ObserverState.UP_TO_DATE );

    assertEquals( observer.isDisposed(), false );

    computedValue.dispose();

    assertEquals( observer.isDisposed(), true );
    assertEquals( observer.getState(), ObserverState.INACTIVE );
  }

  @Test
  public void get_upToDateComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals );
    final Observer observer = computedValue.getObserver();

    setCurrentTransaction( context );

    observer.setState( ObserverState.UP_TO_DATE );
    computedValue.setValue( "XXX" );

    assertEquals( computedValue.get(), "XXX" );
    assertEquals( observer.getState(), ObserverState.UP_TO_DATE );
  }

  @Test
  public void get_staleComputedValue()
    throws Exception
  {
    final ArezContext context = new ArezContext();

    final ComputedValue<String> computedValue =
      new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals );
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
      new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals );
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
      new ComputedValue<>( context, ValueUtil.randomString(), () -> "", Objects::equals );
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
