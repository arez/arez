package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.ComputableValue;
import arez.Flags;
import arez.SafeFunction;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CoreCanNotObserveLowerPriorityIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    captureObserverErrors();

    final ArezContext context = Arez.context();

    final SafeFunction<Integer> f1 = () -> {
      observeADependency();
      return 42;
    };
    final ComputableValue<Integer> computableValue1 = context.computable( f1, Flags.PRIORITY_LOWEST );
    // Attempts to observe lower priority
    final ComputableValue<Integer> computableValue2 = context.computable( () -> computableValue1.get() + 42 );

    observer( computableValue2::get );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: ComputableValue@2 Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0183: Attempting to add observer named 'ComputableValue@2' to ObservableValue named 'ComputableValue@1' where the observer is scheduled at a NORMAL priority but the ObservableValue's owner is scheduled at a LOWEST priority." );
  }
}
