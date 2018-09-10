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
    int _autorunCallCount;

    @Observed
    void autorun()
    {
      // Observe something so it is valid observed
      observeADependency();
      _autorunCallCount++;
    }
  }

  @Test
  public void deferScheduleWillDelayAutorun()
  {
    final TestComponent component = new ObservedNoInvokeTest_Arez_TestComponent();

    assertEquals( component._autorunCallCount, 1 );

    assertInvariant( component::autorun,
                     "Autorun method named 'observed' invoked but @Autorun annotated methods should only be invoked by the runtime." );
    assertEquals( component._autorunCallCount, 1 );
  }
}
