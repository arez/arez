package arez.integration.observed;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservedManualScheduleDisallowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent1
  {
    int _renderCallCount;

    @Observed
    void render()
    {
      getValue();
      _renderCallCount++;
    }

    @ObserverRef
    abstract Observer getRenderObserver();

    @SuppressWarnings( "UnusedReturnValue" )
    @Observable
    abstract long getValue();

    abstract void setValue( long value );
  }

  @Test
  public void scenario()
    throws Exception
  {
    final TestComponent1 component = new ObservedManualScheduleDisallowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );

    // Manual schedule should generate error
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, () -> safeAction( () -> component.getRenderObserver().schedule() ) );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( exception.getMessage(),
                  "Arez-0202: Observer.schedule() invoked on observer named 'TestComponent1.0.render' but supportsManualSchedule() returns false." );
  }
}
