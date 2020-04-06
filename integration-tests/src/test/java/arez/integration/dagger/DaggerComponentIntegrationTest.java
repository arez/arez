package arez.integration.dagger;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DaggerComponentIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( dagger = Feature.ENABLE )
  public static abstract class TestComponent
  {
    private String _value = "";

    @Observable
    String getValue()
    {
      return _value;
    }

    void setValue( @SuppressWarnings( "SameParameterValue" ) final String value )
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

    final ArezContext context = Arez.context();
    context.action( () -> assertEquals( component.getValue(), "" ), ActionFlags.READ_ONLY );
    context.action( () -> component.setValue( "X" ) );
    context.action( () -> assertEquals( component.getValue(), "X" ), ActionFlags.READ_ONLY );
  }
}
