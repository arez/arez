package arez.integration.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ConstructBeforeScheduledTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  public static abstract class TestComponent
  {
    boolean _postConstructCalled;
    boolean _observerCalledBeforePostConstructCalled;
    boolean _observerCalled;

    @PostConstruct
    void postConstruct()
    {
      _postConstructCalled = true;
    }

    @Observed
    void observer()
    {
      // Observe something so it is valid observed
      observeADependency();

      _observerCalled = true;
      if ( !_postConstructCalled )
      {
        _observerCalledBeforePostConstructCalled = true;
      }
    }
  }

  @Test
  public void observerAndPostConstructSequencing()
  {
    final TestComponent component = new ConstructBeforeScheduledTest_Arez_TestComponent();

    assertTrue( component._observerCalled );
    assertTrue( component._postConstructCalled );
    assertFalse( component._observerCalledBeforePostConstructCalled );
  }
}
