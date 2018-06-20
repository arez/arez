package arez.integration.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AutorunNoInvokeTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent
  {
    int _autorunCallCount;

    @Autorun
    void autorun()
    {
      _autorunCallCount++;
    }
  }

  @Test
  public void deferScheduleWillDelayAutorun()
  {
    final TestComponent component = new AutorunNoInvokeTest_Arez_TestComponent();

    assertEquals( component._autorunCallCount, 1 );

    final IllegalStateException exception = expectThrows( IllegalStateException.class, component::autorun );
    assertEquals( exception.getMessage(),
                  "Autorun method named 'autorun' invoked but @Autorun annotated methods should only be invoked by the runtime." );
    assertEquals( component._autorunCallCount, 1 );
  }
}
