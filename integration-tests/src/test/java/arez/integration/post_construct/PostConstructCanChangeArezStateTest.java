package arez.integration.post_construct;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PostConstructCanChangeArezStateTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent
  static abstract class TestComponent
  {
    boolean _postConstructCalled;

    @PostConstruct
    void postConstruct()
    {
      _postConstructCalled = true;
      doSetValue( 42 );
    }

    @Action
    void doSetValue( int value )
    {
      setValue( value );
    }

    @Observable
    abstract int getValue();

    abstract void setValue( int value );
  }

  @Test
  public void scenario()
  {
    final TestComponent component = new PostConstructCanChangeArezStateTest_Arez_TestComponent();

    assertTrue( component._postConstructCalled );
    assertEquals( safeAction( component::getValue ), (Integer) 42 );
  }
}
