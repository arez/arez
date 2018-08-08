package arez.integration.context_ref;

import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.ContextRef;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;

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
    assertInvariant( ContextRefReferencedInCtorTest_Arez_TestComponent::new,
                     "Method named 'getContext' invoked on uninitialized component of type 'TestComponent'" );
  }
}
