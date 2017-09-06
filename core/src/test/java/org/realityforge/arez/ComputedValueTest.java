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

    setCurrentTransaction( context, computedValue.getObserver() );

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

    setCurrentTransaction( context, computedValue.getObserver() );

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
}
