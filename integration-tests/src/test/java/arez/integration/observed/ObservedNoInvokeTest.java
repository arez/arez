package arez.integration.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservedNoInvokeTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent
  {
    int _observerCallCount;

    @Observed
    void observer()
    {
      // Observe something so it is valid observed
      observeADependency();
      _observerCallCount++;
    }
  }

  @Test
  public void deferScheduleWillDelayObserver()
  {
    final TestComponent component = new ObservedNoInvokeTest_Arez_TestComponent();

    assertEquals( component._observerCallCount, 1 );

    assertInvariant( component::observer,
                     "Observed method named 'observer' invoked but @Observed(executor=AREZ) annotated methods should only be invoked by the runtime." );
    assertEquals( component._observerCallCount, 1 );
  }
}
