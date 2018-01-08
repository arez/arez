package arez.integration;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import javax.annotation.PostConstruct;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ConstructBeforeScheduledTest
{
  @ArezComponent
  public static class TestComponent
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
    public void autorun()
    {
      _autorunCalled = true;
      if ( !_postConstructCalled )
      {
        _autorunCalledBeforePostConstructCalled = true;
      }
    }
  }

  @Test
  public void autorunAndPostConstructSequeincing()
  {
    final TestComponent component = new ConstructBeforeScheduledTest_Arez_TestComponent();

    assertTrue( component._autorunCalled );
    assertTrue( component._postConstructCalled );
    assertFalse( component._autorunCalledBeforePostConstructCalled );
  }
}
