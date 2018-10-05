package arez.integration.observe;

import arez.Arez;
import arez.ArezContext;
import arez.ObservableValue;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.integration.AbstractArezIntegrationTest;
import arez.spy.ObserverInfo;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserveArezOrNoDependenciesTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent1
  {
    ObservableValue<?> _observableValue = Arez.context().observable();
    int _renderCallCount;

    @Observe( depType = DepType.AREZ_OR_NONE )
    void render()
    {
      if ( 0 == _renderCallCount )
      {
        _observableValue.reportObserved();
      }
      _renderCallCount++;
    }

    @ObserverRef
    abstract Observer getRenderObserver();
  }

  @Test
  public void scenario()
  {
    final TestComponent1 component = new ObserveArezOrNoDependenciesTest_Arez_TestComponent1();

    final ArezContext context = Arez.context();
    final ObserverInfo info = context.getSpy().asObserverInfo( component.getRenderObserver() );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( info.getDependencies().size(), 1 );

    // should result in re-render invocation but dependencies will go to zero
    context.safeAction( () -> component._observableValue.reportChanged() );

    assertEquals( component._renderCallCount, 2 );
    assertEquals( info.getDependencies().size(), 0 );
  }
}
