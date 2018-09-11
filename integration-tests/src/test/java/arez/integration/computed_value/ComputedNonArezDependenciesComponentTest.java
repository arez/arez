package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.ComputedValueRef;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedNonArezDependenciesComponentTest
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
      element.getComputed();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( () -> assertEquals( element.getComputed(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( () -> element.getComputedComputedValue().reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getComputed(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 2 );

    element._result = "NewValue";

    context.action( () -> element.getComputedComputedValue().reportPossiblyChanged() );

    context.action( () -> assertEquals( element.getComputed(), "NewValue" ) );

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
      return new ComputedNonArezDependenciesComponentTest_Arez_Element( result );
    }

    Element( @Nonnull final String result )
    {
      _result = result;
    }

    @Computed( arezOnlyDependencies = false )
    @Nonnull
    String getComputed()
    {
      _callCount++;
      return _result;
    }

    @ComputedValueRef
    abstract ComputedValue getComputedComputedValue();
  }
}
