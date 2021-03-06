package arez.integration.observe;

import arez.Arez;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObserverInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ApplicationExecutorNoDepsAllowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;

    @Observe( executor = Executor.EXTERNAL, depType = DepType.AREZ_OR_NONE )
    void render()
    {
      _renderCallCount++;
    }

    @OnDepsChange
    void onRenderDepsChange()
    {
    }

    @ObserverRef
    abstract Observer getRenderObserver();
  }

  @Test
  public void scenario()
  {
    final TestComponent1 component = new ApplicationExecutorNoDepsAllowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 0 );

    // Should generate no exception even though there is no observer
    component.render();

    assertEquals( component._renderCallCount, 1 );

    final ObserverInfo info = Arez.context().getSpy().asObserverInfo( component.getRenderObserver() );
    assertEquals( info.getDependencies().size(), 0 );
  }
}
