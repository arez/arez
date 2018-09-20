package arez.test;

import arez.AbstractArezTest;
import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Flags;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueApiTest
  extends AbstractArezTest
{
  @Test
  public void reportPossiblyChanged()
    throws Exception
  {
    final AtomicInteger observerCallCount = new AtomicInteger();
    final AtomicInteger computedCallCount = new AtomicInteger();
    final AtomicInteger result = new AtomicInteger();

    result.set( 42 );

    final ArezContext context = Arez.context();
    final ComputedValue<Integer> computedValue = context.computed( () -> {
      computedCallCount.incrementAndGet();
      return result.get();
    }, Flags.NON_AREZ_DEPENDENCIES );

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
    throws Exception
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
}
