package arez.integration.memoize;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class MemoizeNonArezDependenciesOnActivateComputableValueIntegrationTest
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
    assertEquals( element._activateCount, 1 );
    assertTrue( element.hasMemoizedComputableValue() );

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 1 );
    assertEquals( element._activateCount, 1 );

    context.action( element::reportExternalChange );

    context.action( () -> assertEquals( element.getMemoized(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( element._callCount, 2 );
    assertEquals( element._activateCount, 1 );

    element._result = "NewValue";

    context.action( element::reportExternalChange );

    context.action( () -> assertEquals( element.getMemoized(), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( element._callCount, 3 );
    assertEquals( element._activateCount, 1 );
  }

  @ArezComponent
  public static abstract class Element
  {
    @Nullable
    private ComputableValue<String> _memoizedComputableValue;
    String _result;
    int _callCount;
    int _activateCount;

    @Nonnull
    public static Element create( @Nonnull final String result )
    {
      return new MemoizeNonArezDependenciesOnActivateComputableValueIntegrationTest_Arez_Element( result );
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

    @OnActivate
    void onMemoizedActivate( @Nonnull final ComputableValue<String> computableValue )
    {
      _activateCount++;
      _memoizedComputableValue = computableValue;
    }

    boolean hasMemoizedComputableValue()
    {
      return null != _memoizedComputableValue;
    }

    void reportExternalChange()
    {
      assertNotNull( _memoizedComputableValue );
      _memoizedComputableValue.reportPossiblyChanged();
    }
  }
}
