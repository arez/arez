package arez.integration.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ObserveNoInvokeTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    int _observerCallCount;

    @Observe
    void observer()
    {
      // Observe something so it is valid observed
      observeADependency();
      _observerCallCount++;
    }
  }

  @Test
  public void scenario()
  {
    final TestComponent component = new ObserveNoInvokeTest_Arez_TestComponent();

    assertEquals( component._observerCallCount, 1 );

    assertInvariant( component::observer,
                     "Observe method named 'observer' invoked but @Observe(executor=INTERNAL) annotated methods should only be invoked by the runtime." );
    assertEquals( component._observerCallCount, 1 );
  }
}
