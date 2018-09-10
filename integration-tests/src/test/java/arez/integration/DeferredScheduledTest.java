package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DeferredScheduledTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( deferSchedule = true )
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
    final TestComponent component = new DeferredScheduledTest_Arez_TestComponent();

    assertEquals( component._autorunCallCount, 0 );
    Arez.context().triggerScheduler();
    assertEquals( component._autorunCallCount, 1 );
  }
}
