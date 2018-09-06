package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Flags;
import arez.SafeFunction;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;

@SuppressWarnings( "Duplicates" )
public class CoreCanObserveLowerPriorityIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final SpyEventRecorder recorder = SpyEventRecorder.beginRecording();

    final SafeFunction<Integer> f1 = () -> {
      observeADependency();
      return 42;
    };
    final ComputedValue<Integer> computedValue1 = context.computed( f1, Flags.PRIORITY_LOWEST );
    // f3 observes lower priority
    final SafeFunction<Integer> f2 = () -> computedValue1.get() + 42;
    final ComputedValue<Integer> computedValue2 = context.computed( f2, Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    // f3 observes same priority
    final SafeFunction<Integer> f3 = () -> computedValue2.get() + 42;
    final ComputedValue<Integer> computedValue3 = context.computed( f3, Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    // f4 observes higher priority
    final SafeFunction<Integer> f4 = () -> computedValue3.get() + 42;
    final ComputedValue<Integer> computedValue4 = context.computed( f4, Flags.PRIORITY_LOWEST );

    // Observes same priority
    context.observer( "AR1", computedValue1::get, Flags.PRIORITY_LOWEST );
    // Observes lower priority
    context.observer( "AR4", computedValue4::get, Flags.PRIORITY_HIGH | Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    // Observes higher priority
    context.observer( "AR3", computedValue3::get, Flags.PRIORITY_LOW );

    assertMatchesFixture( recorder );
  }
}
