package arez.integration.sting;

import arez.ActionFlags;
import arez.Arez;
import arez.ArezContext;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.integration.AbstractArezIntegrationTest;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.testng.annotations.Test;
import sting.Injector;
import static org.testng.Assert.*;

public class StingInjectorIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( sting = Feature.ENABLE )
  public static abstract class TestComponent
  {
    @Observable
    abstract String getValue();

    abstract void setValue( String value );
  }

  @Injector( includes = TestComponent.class )
  interface MyInjector
  {
    TestComponent component();
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final MyInjector injector = new StingInjectorIntegrationTest_Sting_MyInjector();
    final TestComponent component = injector.component();

    final ArezContext context = Arez.context();
    context.action( () -> assertNull( component.getValue() ), ActionFlags.READ_ONLY );
    final String value = ValueUtil.randomString();
    context.action( () -> component.setValue( value ) );
    context.action( () -> assertEquals( component.getValue(), value ), ActionFlags.READ_ONLY );
  }
}
