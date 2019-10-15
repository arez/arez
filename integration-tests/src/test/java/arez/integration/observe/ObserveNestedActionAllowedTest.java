package arez.integration.observe;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observe;
import arez.integration.AbstractArezIntegrationTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObserveNestedActionAllowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent1
  {
    int _renderCallCount;
    int _actionCallCount;

    @Observe( nestedActionsAllowed = true )
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
    final TestComponent1 component = new ObserveNestedActionAllowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 1 );

    // This should not trigger render because render not observing as action eliminates dependency
    safeAction( () -> component.setTime( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 1 );

    safeAction( () -> component.setTime2( ValueUtil.randomLong() ) );

    assertEquals( component._renderCallCount, 2 );
    assertEquals( component._actionCallCount, 2 );
  }
}
