package arez.integration.observe;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class TrackNoNestActionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent1
  {
    int _renderCallCount;
    int _depsChangedCallCount;
    int _actionCallCount;

    @Observe( executor = Executor.EXTERNAL )
    public void render()
    {
      getTime2();
      _renderCallCount++;
      myAction();
    }

    @OnDepsChange
    void onRenderDepsChange()
    {
      _depsChangedCallCount++;
    }

    @Action( mutation = false, requireNewTransaction = true )
    void myAction()
    {
      getTime();
      _actionCallCount++;
    }

    @Observable
    abstract long getTime();

    abstract void setTime( long value );

    @Observable
    abstract long getTime2();

    abstract void setTime2( long value );
  }

  @Test
  public void scenario()
  {
    final TestComponent1 component = new TrackNoNestActionTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 0 );
    assertEquals( component._actionCallCount, 0 );
    assertEquals( component._depsChangedCallCount, 0 );

    assertInvariant( component::render,
                     "Arez-0187: Attempting to nest action named 'arez_integration_observe_TrackNoNestActionTest_TestComponent1.1.myAction' inside transaction named 'arez_integration_observe_TrackNoNestActionTest_TestComponent1.1.render' created by an observer that does not allow nested actions." );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 0 );
    assertEquals( component._depsChangedCallCount, 0 );

    // This should not trigger renderDepsUpdated flag as render not observing as action obscures dependency
    safeAction( () -> component.setTime( 33L ) );

    assertEquals( component._depsChangedCallCount, 0 );

    safeAction( () -> component.setTime2( 33L ) );

    assertEquals( component._depsChangedCallCount, 1 );
  }
}
