package arez.integration.observed;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Observed;
import arez.integration.AbstractArezIntegrationTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ObservedNestedActionDisallowedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent1
  {
    int _renderCallCount;
    int _actionCallCount;

    @Observed
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
    setIgnoreObserverErrors( true );
    final TestComponent1 component = new ObservedNestedActionDisallowedTest_Arez_TestComponent1();

    assertEquals( component._renderCallCount, 1 );
    assertEquals( component._actionCallCount, 0 );

    assertEquals( getObserverErrors().size(), 1 );
    assertEquals( getObserverErrors().get( 0 ),
                  "Observer: TestComponent1.0.render Error: REACTION_ERROR java.lang.IllegalStateException: Arez-0187: Attempting to nest action named 'TestComponent1.0.myAction' inside transaction named 'TestComponent1.0.render' created by an observer that does not allow nested actions." );

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
