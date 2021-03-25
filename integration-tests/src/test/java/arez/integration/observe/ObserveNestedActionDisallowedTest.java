package arez.integration.observe;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.integration.AbstractArezIntegrationTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserveNestedActionDisallowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;
    int _actionCallCount;

    @Observe
    void render()
    {
      getTime2();
      _renderCallCount++;
      myAction();
    }

    @Action( mutation = false, requireNewTransaction = true )
    void myAction()
    {
      getTime();
      _actionCallCount++;
    }

    @SuppressWarnings( "UnusedReturnValue" )
    @Observable
    abstract long getTime();

    abstract void setTime( long value );

    @SuppressWarnings( "UnusedReturnValue" )
    @Observable
    abstract long getTime2();

    abstract void setTime2( long value );
  }

  @Test
  public void scenario()
  {
    captureObserverErrors();
    final TestComponent1 component = new ObserveNestedActionDisallowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 0 );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: arez_integration_observe_ObserveNestedActionDisallowedTest_TestComponent1.1.render Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0187: Attempting to nest action named 'arez_integration_observe_ObserveNestedActionDisallowedTest_TestComponent1.1.myAction' inside transaction named 'arez_integration_observe_ObserveNestedActionDisallowedTest_TestComponent1.1.render' created by an observer that does not allow nested actions." );

    // This should not trigger render because render not observing as action eliminates dependency
    safeAction( () -> component.setTime( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 0 );

    safeAction( () -> component.setTime2( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 2 );
    assertEquals( component._actionCallCount, 0 );

    assertEquals( getObserverErrors().size(), 2 );
  }
}
