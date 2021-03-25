package arez.integration;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class AccessingDisposedTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    int invokeCount;

    @Action
    void myAction()
    {
      Arez.context().observable().reportObserved();
      invokeCount++;
    }
  }

  @Test
  public void accessingDisposedComponentResultsInError()
  {
    final TestComponent component = new AccessingDisposedTest_Arez_TestComponent();

    assertEquals( component.invokeCount, 0 );

    component.myAction();

    assertEquals( component.invokeCount, 1 );

    Disposable.dispose( component );

    assertTrue( Disposable.isDisposed( component ) );
    assertInvariant( component::myAction,
                     "Method named 'myAction' invoked on disposed component named 'arez_integration_AccessingDisposedTest_TestComponent.1'" );
  }
}
