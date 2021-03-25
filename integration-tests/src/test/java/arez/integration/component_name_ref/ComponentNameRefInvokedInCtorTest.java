package arez.integration.component_name_ref;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentNameRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public final class ComponentNameRefInvokedInCtorTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    TestComponent()
    {
      getName();
    }

    @SuppressWarnings( "UnusedReturnValue" )
    @ComponentNameRef
    abstract String getName();
  }

  @Test
  public void scenario()
  {
    assertInvariant( ComponentNameRefInvokedInCtorTest_Arez_TestComponent::new,
                     "Method named 'getName' invoked on uninitialized component of type 'arez_integration_component_name_ref_ComponentNameRefInvokedInCtorTest_TestComponent'" );
  }
}
