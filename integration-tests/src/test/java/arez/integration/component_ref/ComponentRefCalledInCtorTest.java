package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public class ComponentRefCalledInCtorTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    TestComponent()
    {
      getComponent();
    }

    @ComponentRef
    abstract Component getComponent();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    assertInvariant( ComponentRefCalledInCtorTest_Arez_TestComponent::new,
                     "Method named 'getComponent' invoked on uninitialized component of type 'TestComponent'" );
  }
}
