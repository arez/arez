package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Options;
import arez.SafeFunction;
import arez.integration.AbstractArezIntegrationTest;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedNonArezDependenciesTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
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
      context.computed( "TestComputed", action, Options.MANUAL_REPORT_STALE_ALLOWED | Options.KEEPALIVE );

    final AtomicInteger autorunCallCount = new AtomicInteger();
    autorun( () -> {
      autorunCallCount.incrementAndGet();
      computedValue.get();
    } );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( () -> assertEquals( computedValue.get(), "" ) );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( calls.get(), 1 );

    context.action( computedValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computedValue.get(), "" ) );

    assertEquals( autorunCallCount.get(), 1 );
    assertEquals( calls.get(), 2 );

    result.set( "NewValue" );

    context.action( computedValue::reportPossiblyChanged );

    context.action( () -> assertEquals( computedValue.get(), "NewValue" ) );

    assertEquals( autorunCallCount.get(), 2 );
    assertEquals( calls.get(), 3 );
  }
}
