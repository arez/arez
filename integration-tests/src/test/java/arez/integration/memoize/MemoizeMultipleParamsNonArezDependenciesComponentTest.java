package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObservableValueInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MemoizeMultipleParamsNonArezDependenciesComponentTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create( "" );

    final AtomicInteger observerCallCount = new AtomicInteger();
    observer( () -> {
      observerCallCount.incrementAndGet();
      element.getMemoized( 3 );
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element.getCallCount( 3 ), 1 );

    context.action( () -> assertEquals( element.getMemoized( 3 ), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element.getCallCount( 3 ), 1 );

    context.action( () -> element.getMemoizedComputableValue( 3 ).reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getMemoized( 3 ), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element.getCallCount( 3 ), 2 );

    element._result = "NewValue";

    context.action( () -> element.getMemoizedComputableValue( 3 ).reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getMemoized( 3 ), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( element.getCallCount( 3 ), 3 );
  }

  @Test
  public void lazyValidationDoesNotTrackTransitiveParameterizedMemoizeDependencies()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create( "" );

    final AtomicInteger observerCallCount = new AtomicInteger();
    observer( () -> {
      observerCallCount.incrementAndGet();
      assertEquals( element.getOuterMemoized( 3 ), "" );
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( context,
                        element.getOuterMemoizedComputableValue( 3 ),
                        element.getNestedMemoizedComputableValue( 3 ) );
    assertDependencies( context,
                        element.getNestedMemoizedComputableValue( 3 ),
                        element.getMemoizedComputableValue( 3 ) );

    context.action( () -> element.getMemoizedComputableValue( 3 ).reportPossiblyChanged() );
    context.action( () -> assertEquals( element.getOuterMemoized( 3 ), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( context,
                        element.getOuterMemoizedComputableValue( 3 ),
                        element.getNestedMemoizedComputableValue( 3 ) );
    assertDependencies( context,
                        element.getNestedMemoizedComputableValue( 3 ),
                        element.getMemoizedComputableValue( 3 ) );
    assertNotObserver( context,
                       element.getMemoizedComputableValue( 3 ),
                       element.getOuterMemoizedComputableValue( 3 ) );
  }

  private static void assertDependencies( @Nonnull final ArezContext context,
                                          @Nonnull final ComputableValue<?> computableValue,
                                          @Nonnull final ComputableValue<?>... dependencies )
  {
    final List<ObservableValueInfo> actual = context.getSpy().asComputableValueInfo( computableValue ).getDependencies();
    assertEquals( actual.size(), dependencies.length );
    for ( int i = 0; i < dependencies.length; i++ )
    {
      assertEquals( actual.get( i ).getName(), dependencies[ i ].getName() );
    }
  }

  private static void assertNotObserver( @Nonnull final ArezContext context,
                                         @Nonnull final ComputableValue<?> observable,
                                         @Nonnull final ComputableValue<?> observer )
  {
    assertFalse( context.getSpy().asComputableValueInfo( observable ).getObservers().stream().
      anyMatch( o -> o.getName().equals( observer.getName() ) ) );
    assertFalse( context.getSpy().asComputableValueInfo( observer ).getDependencies().stream().
      anyMatch( o -> o.getName().equals( observable.getName() ) ) );
  }

  @SuppressWarnings( "SameParameterValue" )
  @ArezComponent
  public static abstract class Element
  {
    private final Map<Integer, AtomicInteger> _callCounts = new HashMap<>();
    private final Map<Integer, AtomicInteger> _nestedCallCounts = new HashMap<>();
    private final Map<Integer, AtomicInteger> _outerCallCounts = new HashMap<>();
    String _result;

    @Nonnull
    public static Element create( @Nonnull final String result )
    {
      return new MemoizeMultipleParamsNonArezDependenciesComponentTest_Arez_Element( result );
    }

    Element( @Nonnull final String result )
    {
      _result = result;
    }

    @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
    @Nonnull
    String getMemoized( final int index )
    {
      _callCounts.computeIfAbsent( index, i -> new AtomicInteger() ).incrementAndGet();
      return _result;
    }

    int getCallCount( final int index )
    {
      return _callCounts.computeIfAbsent( index, i -> new AtomicInteger() ).get();
    }

    @ComputableValueRef
    abstract ComputableValue<String> getMemoizedComputableValue( int index );

    @Memoize
    @Nonnull
    String getNestedMemoized( final int index )
    {
      _nestedCallCounts.computeIfAbsent( index, i -> new AtomicInteger() ).incrementAndGet();
      return getMemoized( index );
    }

    @ComputableValueRef
    abstract ComputableValue<String> getNestedMemoizedComputableValue( int index );

    @Memoize
    @Nonnull
    String getOuterMemoized( final int index )
    {
      _outerCallCounts.computeIfAbsent( index, i -> new AtomicInteger() ).incrementAndGet();
      return getNestedMemoized( index );
    }

    @ComputableValueRef
    abstract ComputableValue<String> getOuterMemoizedComputableValue( int index );
  }
}
