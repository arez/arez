package arez.integration;

import arez.Arez;
import arez.Flags;
import arez.Procedure;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import dagger.Component;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DaggerComponentIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( dagger = Feature.ENABLE )
  static abstract class TestComponent
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

    final Procedure executable1 = () -> assertEquals( component.getValue(), "" );
    Arez.context().action( executable1, Flags.READ_ONLY );
    Arez.context().action( () -> component.setValue( "X" ) );
    final Procedure executable = () -> assertEquals( component.getValue(), "X" );
    Arez.context().action( executable, Flags.READ_ONLY );
  }
}
