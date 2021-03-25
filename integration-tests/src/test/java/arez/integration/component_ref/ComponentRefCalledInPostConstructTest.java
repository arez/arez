package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public final class ComponentRefCalledInPostConstructTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    @PostConstruct
    void postConstruct()
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
    assertInvariant( ComponentRefCalledInPostConstructTest_Arez_TestComponent::new,
                     "Method named 'getComponent' invoked on incomplete component named 'arez_integration_component_ref_ComponentRefCalledInPostConstructTest_TestComponent.1'" );
  }
}
