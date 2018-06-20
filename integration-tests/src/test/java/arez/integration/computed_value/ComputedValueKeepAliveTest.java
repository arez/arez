package arez.integration.computed_value;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.EqualityComparator;
import arez.Priority;
import arez.SafeFunction;
import arez.integration.AbstractIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComputedValueKeepAliveTest
  extends AbstractIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final AtomicInteger calls = new AtomicInteger();
    final SafeFunction<String> action = () -> {
      calls.incrementAndGet();
      return "";
    };
    final ComputedValue<String> computedValue =
      context.createComputedValue( null,
                                   "TestComputed",
                                   action,
                                   EqualityComparator.defaultComparator(),
                                   null,
                                   null,
                                   null,
                                   null,
                                   Priority.NORMAL,
                                   true,
                                   true );

    assertEquals( calls.get(), 1 );

    context.action( "Get 1", computedValue::get );

    assertEquals( calls.get(), 1 );

    context.action( "Get 2", computedValue::get );

    assertEquals( calls.get(), 1 );

    computedValue.dispose();

    assertMatchesFixture( recorder );
  }
}
