package arez.integration.priority;

import arez.Arez;
import arez.ArezContext;
import arez.ComputedValue;
import arez.Priority;
import arez.SafeFunction;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class CoreCanNotObserveLowerPriorityIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    setIgnoreObserverErrors( true );
    setPrintObserverErrors( false );

    final ArezContext context = Arez.context();

    final SafeFunction<Integer> f1 = () -> {
      observeADependency();
      return 42;
    };
    final ComputedValue<Integer> computedValue1 =
      context.computed( null,
                        null,
                        f1,
                        null,
                        null,
                        null,
                        null,
                        Priority.LOWEST );
    // Attempts to observe lower priority
    final ComputedValue<Integer> computedValue2 = context.computed( () -> computedValue1.get() + 42 );

    autorun( computedValue2::get );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: ComputedValue@2 Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0183: Attempting to add observer named 'ComputedValue@2' to ObservableValue named 'ComputedValue@1' where the observer is scheduled at a NORMAL priority but the ObservableValue's owner is scheduled at a LOWEST priority." );
  }
}
