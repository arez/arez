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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MemoizeNonArezDependenciesComponentTest
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
      element.getMemoized();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( () -> element.getMemoizedComputableValue().reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 2 );

    element._result = "NewValue";

    context.action( () -> element.getMemoizedComputableValue().reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getMemoized(), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( element._callCount, 3 );
  }

  @Test
  public void lazyValidationDoesNotTrackTransitiveMemoizeDependencies()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create( "" );

    final AtomicInteger observerCallCount = new AtomicInteger();
    observer( () -> {
      observerCallCount.incrementAndGet();
      assertEquals( element.getOuterMemoized(), "" );
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( context,
                        element.getOuterMemoizedComputableValue(),
                        element.getNestedMemoizedComputableValue() );
    assertDependencies( context,
                        element.getNestedMemoizedComputableValue(),
                        element.getMemoizedComputableValue() );

    context.action( () -> element.getMemoizedComputableValue().reportPossiblyChanged() );
    context.action( () -> assertEquals( element.getOuterMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertDependencies( context,
                        element.getOuterMemoizedComputableValue(),
                        element.getNestedMemoizedComputableValue() );
    assertDependencies( context,
                        element.getNestedMemoizedComputableValue(),
                        element.getMemoizedComputableValue() );
    assertNotObserver( context, element.getMemoizedComputableValue(), element.getOuterMemoizedComputableValue() );
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

  @ArezComponent
  public static abstract class Element
  {
    String _result;
    int _callCount;
    int _nestedCallCount;
    int _outerCallCount;

    @Nonnull
    public static Element create( @Nonnull final String result )
    {
      return new MemoizeNonArezDependenciesComponentTest_Arez_Element( result );
    }

    Element( @Nonnull final String result )
    {
      _result = result;
    }

    @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
    @Nonnull
    String getMemoized()
    {
      _callCount++;
      return _result;
    }

    @ComputableValueRef
    abstract ComputableValue<String> getMemoizedComputableValue();

    @Memoize
    @Nonnull
    String getNestedMemoized()
    {
      _nestedCallCount++;
      return getMemoized();
    }

    @ComputableValueRef
    abstract ComputableValue<String> getNestedMemoizedComputableValue();

    @Memoize
    @Nonnull
    String getOuterMemoized()
    {
      _outerCallCount++;
      return getNestedMemoized();
    }

    @ComputableValueRef
    abstract ComputableValue<String> getOuterMemoizedComputableValue();
  }
}
