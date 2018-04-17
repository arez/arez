package arez.integration.component_ref;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import arez.integration.AbstractIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentRefCalledInCtorTest
  extends AbstractIntegrationTest
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
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, ComponentRefCalledInCtorTest_Arez_TestComponent::new );
    assertEquals( exception.getMessage(),
                  "Method named 'getComponent' invoked on uninitialized component of type 'TestComponent'" );
  }
}
