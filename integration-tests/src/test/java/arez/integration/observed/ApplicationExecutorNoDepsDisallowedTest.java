package arez.integration.observed;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ApplicationExecutorNoDepsDisallowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;

    @Observed( executor = Executor.APPLICATION, depType = DepType.AREZ )
    void render()
    {
      _renderCallCount++;
    }

    @OnDepsChanged
    final void onRenderDepsChanged()
    {
    }
  }

  @Test
  public void scenario()
  {
    final TestComponent1 component = new ApplicationExecutorNoDepsDisallowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 0 );

    assertInvariantFailure( component::render,
                            "Arez-0118: Observer named 'TestComponent1.0.render' completed observed function (executed by application) but is not observing any properties." );

    assertEquals( component._renderCallCount, 1 );
  }
}
