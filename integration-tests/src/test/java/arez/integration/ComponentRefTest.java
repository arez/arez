package arez.integration;

import arez.Component;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ComponentRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static class TestComponent
  {
    @ComponentRef
    Component getComponent()
    {
      throw new IllegalStateException();
    }
  }

  @Test
  public void componentRef()
    throws Throwable
  {
    final TestComponent instance = new ComponentRefTest_Arez_TestComponent();
    final Component component = instance.getComponent();
    assertNotNull( component );
    assertEquals( component.getType(), "TestComponent" );
  }
}
