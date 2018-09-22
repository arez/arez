package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Flags;
import arez.SafeFunction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueApiTest
  extends AbstractArezTest
{
  @Test
  public void reportPossiblyChanged()
  {
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ComputedValue<Integer> computedValue = context.computed( () -> {
      computedCallCount.incrementAndGet();
      return result.get();
    }, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES );

    assertEquals( computedCallCount.get(), 0 );
    assertEquals( observerCallCount.get(), 0 );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( computedCallCount.get(), 1 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computedValue.get(), (Integer) 42 ) );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( computedCallCount.get(), 2 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computedValue.get(), (Integer) 42 ) );

    result.set( 21 );

    context.safeAction( computedValue::reportPossiblyChanged );

    assertEquals( computedCallCount.get(), 3 );
    assertEquals( observerCallCount.get(), 2 );

    context.safeAction( () -> assertEquals( computedValue.get(), (Integer) 21 ) );
  }

  @Test
  public void computedWithNoDependencies()
  {
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger computedCallCount = new AtomicInteger();

    final ArezContext context = Arez.context();
    final ComputedValue<Integer> computedValue = context.computed( () -> {
      computedCallCount.incrementAndGet();
      return 1;
    }, Flags.AREZ_OR_NO_DEPENDENCIES );

    assertEquals( computedCallCount.get(), 0 );
    assertEquals( observerCallCount.get(), 0 );

    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( computedCallCount.get(), 1 );
    assertEquals( observerCallCount.get(), 1 );

    context.safeAction( () -> assertEquals( computedValue.get(), (Integer) 1 ) );
  }

  @Test
  public void arezOrExternalDependencies()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final AtomicInteger calls = new AtomicInteger();
    final AtomicReference<String> result = new AtomicReference<>();
    result.set( "" );
    final SafeFunction<String> action = () -> {
      calls.incrementAndGet();
      return result.get();
    };
    final ComputedValue<String> computedValue =
      context.computed( "TestComputed", action, Flags.AREZ_OR_EXTERNAL_DEPENDENCIES | Flags.KEEPALIVE );

    final AtomicInteger observerCallCount = new AtomicInteger();
    context.observer( () -> {
      observerCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( () -> assertEquals( computedValue.get(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( computedValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computedValue.get(), "" ) );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( calls.get(), 2 );

    result.set( "NewValue" );

    context.action( computedValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computedValue.get(), "NewValue" ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( calls.get(), 3 );
  }
}
