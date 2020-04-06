package arez.integration.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ConstructBeforeScheduledTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    boolean _postConstructCalled;
    boolean _observerCalledBeforePostConstructCalled;
    boolean _observerCalled;

    @PostConstruct
    void postConstruct()
    {
      _postConstructCalled = true;
    }

    @Observe
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
