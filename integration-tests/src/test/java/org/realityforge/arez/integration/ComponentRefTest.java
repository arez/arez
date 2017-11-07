package org.realityforge.arez.integration;

import org.realityforge.arez.Component;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentRef;
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
    final TestComponent instance = new ComponentRefTest$Arez_TestComponent();
    final Component component = instance.getComponent();
    assertNotNull( component );
    assertEquals( component.getType(), "TestComponent" );
  }
}
