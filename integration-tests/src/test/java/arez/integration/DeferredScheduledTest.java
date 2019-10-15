package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DeferredScheduledTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( deferSchedule = true )
  static abstract class TestComponent
  {
    int _observerCallCount;

    @Observe
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
    final TestComponent component = new DeferredScheduledTest_Arez_TestComponent();

    assertEquals( component._observerCallCount, 0 );
    Arez.context().triggerScheduler();
    assertEquals( component._observerCallCount, 1 );
  }
}
