package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MemoizeNonArezDependenciesComponentTest
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

  @ArezComponent
  public static abstract class Element
  {
    String _result;
    int _callCount;

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
  }
}
