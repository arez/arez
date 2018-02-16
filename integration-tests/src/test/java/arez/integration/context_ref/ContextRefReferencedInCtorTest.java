package arez.integration.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import arez.integration.AbstractIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ContextRefReferencedInCtorTest
  extends AbstractIntegrationTest
{
  @ArezComponent( allowEmpty = true )
  static abstract class TestComponent
  {
    TestComponent()
    {
      getContext();
    }

    @ContextRef
    abstract ArezContext getContext();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final IllegalStateException exception =
      expectThrows( IllegalStateException.class, ContextRefReferencedInCtorTest_Arez_TestComponent::new );
    assertEquals( exception.getMessage(), "Method invoked on uninitialized component of type 'TestComponent'" );
  }
}
