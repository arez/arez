package org.realityforge.arez.integration;

import org.realityforge.arez.Arez;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ContextRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ContextRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static class TestComponent
  {
    @ContextRef
    ArezContext getContext()
    {
      throw new IllegalStateException();
    }
  }

  @Test
  public void contextRef()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ContextRefTest$Arez_TestComponent();
    assertEquals( component.getContext(), context );
  }
}
