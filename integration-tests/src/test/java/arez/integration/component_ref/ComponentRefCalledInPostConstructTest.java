package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

public class ComponentRefCalledInPostConstructTest
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

    @ComponentRef
    abstract Component getComponent();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    assertInvariant( ComponentRefCalledInPostConstructTest_Arez_TestComponent::new,
                     "Method named 'getComponent' invoked on incomplete component named 'TestComponent.0'" );
  }
}
