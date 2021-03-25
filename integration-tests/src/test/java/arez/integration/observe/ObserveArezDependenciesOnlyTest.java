package arez.integration.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserveArezDependenciesOnlyTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;

    @Observe
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
  {
    final TestComponent1 component = new ObserveArezDependenciesOnlyTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );

    // reportStale should result in exception
    assertInvariant( () -> safeAction( () -> component.getRenderObserver().reportStale() ),
                     "Arez-0199: Observer.reportStale() invoked on observer named 'arez_integration_observe_ObserveArezDependenciesOnlyTest_TestComponent1.1.render' but the observer has not specified AREZ_OR_EXTERNAL_DEPENDENCIES flag." );

    assertEquals( component._renderCallCount, 1 );
  }
}
