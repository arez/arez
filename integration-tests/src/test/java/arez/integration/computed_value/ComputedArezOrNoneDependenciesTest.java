package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedArezOrNoneDependenciesTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final Element element = Element.create();

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

    element._result = "NewValue";

    context.action( () -> assertEquals( element.getComputed(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( element._observableValue::reportChanged );

    context.action( () -> assertEquals( element.getComputed(), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( element._callCount, 2 );
  }

  @ArezComponent
  public static abstract class Element
  {
    @Nonnull
    final ObservableValue<?> _observableValue;
    String _result;
    int _callCount;

    @Nonnull
    public static Element create()
    {
      return new ComputedArezOrNoneDependenciesTest_Arez_Element();
    }

    Element()
    {
      _observableValue = Arez.context().observable();
      _result = "";
    }

    @Computed( depType = DepType.AREZ_OR_NONE )
    @Nonnull
    String getComputed()
    {
      if ( 0 == _callCount )
      {
        _observableValue.reportObserved();
      }
      _callCount++;
      return _result;
    }
  }
}
