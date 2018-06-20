package arez.integration.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ContextRefReferencedInCtorTest
  extends AbstractArezIntegrationTest
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
    assertEquals( exception.getMessage(),
                  "Method named 'getContext' invoked on uninitialized component of type 'TestComponent'" );
  }
}
