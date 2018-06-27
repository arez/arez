package arez.integration.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
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
    boolean _autorunCalledBeforePostConstructCalled;
    boolean _autorunCalled;

    @PostConstruct
    void postConstruct()
    {
      _postConstructCalled = true;
    }

    @Autorun
    void autorun()
    {
      // Observe something so it is valid autorun
      observeADependency();

      _autorunCalled = true;
      if ( !_postConstructCalled )
      {
        _autorunCalledBeforePostConstructCalled = true;
      }
    }
  }

  @Test
  public void autorunAndPostConstructSequencing()
  {
    final TestComponent component = new ConstructBeforeScheduledTest_Arez_TestComponent();

    assertTrue( component._autorunCalled );
    assertTrue( component._postConstructCalled );
    assertFalse( component._autorunCalledBeforePostConstructCalled );
  }
}
