package arez.integration.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class TrackCanNotBeNestedInActionTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;

    @Observe( executor = Executor.EXTERNAL )
    void render()
    {
      observeADependency();
      _renderCallCount++;
    }

    @OnDepsChange
    void onRenderDepsChange()
    {
    }
  }

  @Test
  public void scenario()
  {
    final TestComponent1 component = new TrackCanNotBeNestedInActionTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 0 );

    // Call outside action
    component.render();

    assertEquals( component._renderCallCount, 1 );

    // Call observe inside action. This should generate an exception
    assertInvariant( () -> safeAction( component::render ),
                     "Arez-0171: Attempting to create a tracking transaction named 'arez_integration_observe_TrackCanNotBeNestedInActionTest_TestComponent1.1.render' for the observer named 'arez_integration_observe_TrackCanNotBeNestedInActionTest_TestComponent1.1.render' but the transaction is not a top-level transaction when this is required. This may be a result of nesting a observe() call inside an action or another observer function." );

    assertEquals( component._renderCallCount, 1 );
  }
}
