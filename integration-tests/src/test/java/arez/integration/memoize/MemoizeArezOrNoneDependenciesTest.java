package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MemoizeArezOrNoneDependenciesTest
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
      element.getMemoized();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    element._result = "NewValue";

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );

    context.action( element._observableValue::reportChanged );

    context.action( () -> assertEquals( element.getMemoized(), "NewValue" ) );

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
      return new MemoizeArezOrNoneDependenciesTest_Arez_Element();
    }

    Element()
    {
      _observableValue = Arez.context().observable();
      _result = "";
    }

    @Memoize( depType = DepType.AREZ_OR_NONE )
    @Nonnull
    String getMemoized()
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
