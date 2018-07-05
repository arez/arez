package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Priority;
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
    final ComputedValue<Integer> computedValue1 =
      context.computedValue( null,
                             null,
                             f1,
                             null,
                             null,
                             null,
                             null,
                             Priority.LOWEST,
                             false,
                             true,
                             false );
    // f3 observes lower priority
    final SafeFunction<Integer> f2 = () -> computedValue1.get() + 42;
    final ComputedValue<Integer> computedValue2 =
      context.computedValue( null,
                             null,
                             f2,
                             null,
                             null,
                             null,
                             null,
                             Priority.NORMAL,
                             false,
                             true,
                             true );
    // f3 observes same priority
    final SafeFunction<Integer> f3 = () -> computedValue2.get() + 42;
    final ComputedValue<Integer> computedValue3 =
      context.computedValue( null,
                             null,
                             f3,
                             null,
                             null,
                             null,
                             null,
                             Priority.NORMAL,
                             false,
                             true,
                             true );

    // f4 observes higher priority
    final SafeFunction<Integer> f4 = () -> computedValue3.get() + 42;
    final ComputedValue<Integer> computedValue4 =
      context.computedValue( null,
                             null,
                             f4,
                             null,
                             null,
                             null,
                             null,
                             Priority.LOWEST,
                             false,
                             true,
                             false );

    // Observes same priority
    context.autorun( null, "AR1", false, computedValue1::get, Priority.LOWEST, true, false );
    // Observes lower priority
    context.autorun( null, "AR4", false, computedValue4::get, Priority.HIGH, true, true );
    // Observes higher priority
    context.autorun( null, "AR3", false, computedValue3::get, Priority.LOW, true, false );

    assertMatchesFixture( recorder );
  }
}
