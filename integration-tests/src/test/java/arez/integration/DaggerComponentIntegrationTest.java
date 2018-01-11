package arez.integration;

import arez.Arez;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import dagger.Component;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DaggerComponentIntegrationTest
{
  @ArezComponent( dagger = Feature.ENABLE )
  static class TestComponent
  {
    private String _value = "";

    @Observable
    String getValue()
    {
      return _value;
    }

    void setValue( final String value )
    {
      _value = value;
    }
  }

  @Singleton
  @Component( modules = DaggerComponentIntegrationTest_TestComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    TestComponent component();
  }

  @Test
  public void useDaggerComponentToGetAccessToComponent()
    throws Throwable
  {
    final TestDaggerComponent daggerComponent = DaggerDaggerComponentIntegrationTest_TestDaggerComponent.create();
    final TestComponent component = daggerComponent.component();

    Arez.context().action( false, () -> assertEquals( component.getValue(), "" ) );
    Arez.context().action( true, () -> component.setValue( "X" ) );
    Arez.context().action( false, () -> assertEquals( component.getValue(), "X" ) );
  }
}
