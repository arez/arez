package arez.integration.observe;

import arez.Arez;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserveManualScheduleTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;
    int _depsChangedCallCount;

    @Observe
    void render()
    {
      getValue();
      _renderCallCount++;
    }

    @OnDepsChange
    void onRenderDepsChange()
    {
      _depsChangedCallCount++;
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
  {
    final TestComponent1 component = new ObserveManualScheduleTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._depsChangedCallCount, 0 );

    // Scheduling should have no impact as not dirty
    Arez.context().safeAction( () -> component.getRenderObserver().schedule() );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._depsChangedCallCount, 0 );

    // Trigger a dependency change, mark observer as dirty
    safeAction( () -> component.setValue( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._depsChangedCallCount, 1 );

    // Trigger a dependency change - but should not invoke onRenderDepsChange as still dirty
    safeAction( () -> component.setValue( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._depsChangedCallCount, 1 );

    // Scheduling should actually run render!
    Arez.context().safeAction( () -> component.getRenderObserver().schedule() );

    assertEquals( component._renderCallCount, 2 );
    assertEquals( component._depsChangedCallCount, 1 );
  }
}
