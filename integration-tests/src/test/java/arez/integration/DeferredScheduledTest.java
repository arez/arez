package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DeferredScheduledTest
{
  @ArezComponent( deferSchedule = true )
  public static class TestComponent
  {
    int _autorunCallCount;

    @Autorun
    public void autorun()
    {
      _autorunCallCount++;
    }
  }

  @Test
  public void autorunAndPostConstructSequeincing()
  {
    final TestComponent component = new DeferredScheduledTest_Arez_TestComponent();

    assertEquals( component._autorunCallCount, 0 );
    Arez.context().triggerScheduler();
    assertEquals( component._autorunCallCount, 1 );
  }
}
