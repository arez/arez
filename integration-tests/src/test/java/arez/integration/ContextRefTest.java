package arez.integration;

import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ContextRefTest
  extends AbstractIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    @ContextRef
    abstract ArezContext getContext();
  }

  @Test
  public void contextRef()
    throws Throwable
  {
    final ArezContext context = Arez.context();

    final TestComponent component = new ContextRefTest_Arez_TestComponent();
    assertEquals( component.getContext(), context );
  }
}
