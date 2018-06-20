package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

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
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, ComponentRefCalledInPostConstructTest_Arez_TestComponent::new );
    assertEquals( exception.getMessage(),
                  "Method named 'getComponent' invoked on incomplete component named 'TestComponent.0'" );
  }
}
