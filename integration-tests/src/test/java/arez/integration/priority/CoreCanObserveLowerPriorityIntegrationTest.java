package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.Observer;
import arez.SafeFunction;
import arez.integration.AbstractArezIntegrationTest;
import arez.integration.util.SpyEventRecorder;
import org.testng.annotations.Test;

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
    final ComputableValue<Integer> computableValue1 = context.computable( f1, ComputableValue.Flags.PRIORITY_LOWEST );
    // f3 observes lower priority
    final SafeFunction<Integer> f2 = () -> computableValue1.get() + 42;
    final ComputableValue<Integer> computableValue2 = context.computable( f2, ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    // f3 observes same priority
    final SafeFunction<Integer> f3 = () -> computableValue2.get() + 42;
    final ComputableValue<Integer> computableValue3 = context.computable( f3, ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );

    // f4 observes higher priority
    final SafeFunction<Integer> f4 = () -> computableValue3.get() + 42;
    final ComputableValue<Integer> computableValue4 = context.computable( f4, ComputableValue.Flags.PRIORITY_LOWEST );

    // Observes same priority
    context.observer( "AR1", computableValue1::get, ComputableValue.Flags.PRIORITY_LOWEST );
    // Observes lower priority
    context.observer( "AR4", computableValue4::get, ComputableValue.Flags.PRIORITY_HIGH | ComputableValue.Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES );
    // Observes higher priority
    context.observer( "AR3", computableValue3::get, ComputableValue.Flags.PRIORITY_LOW );

    assertMatchesFixture( recorder );
  }
}
