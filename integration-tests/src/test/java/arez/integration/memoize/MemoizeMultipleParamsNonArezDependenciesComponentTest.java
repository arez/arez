package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MemoizeMultipleParamsNonArezDependenciesComponentTest
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

  @ArezComponent
  public static abstract class Element
  {
    private final Map<Integer, AtomicInteger> _callCounts = new HashMap<>();
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
  }
}
