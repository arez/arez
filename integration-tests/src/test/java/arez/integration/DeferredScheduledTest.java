package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DeferredScheduledTest
  extends AbstractIntegrationTest
{
  @ArezComponent( deferSchedule = true )
  public static abstract class TestComponent
  {
    int _autorunCallCount;

    @Autorun
    void autorun()
    {
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
