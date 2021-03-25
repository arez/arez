package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public final class ComponentRefCalledInCtorTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    TestComponent()
    {
      getComponent();
    }

    @SuppressWarnings( "UnusedReturnValue" )
    @ComponentRef
    abstract Component getComponent();
  }

  @Test
  public void scenario()
  {
    assertInvariant( ComponentRefCalledInCtorTest_Arez_TestComponent::new,
                     "Method named 'getComponent' invoked on uninitialized component of type 'arez_integration_component_ref_ComponentRefCalledInCtorTest_TestComponent'" );
  }
}
