package arez.integration.dagger.factory;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NoInjectionsIntegrationTest
  extends AbstractArezIntegrationTest
{
  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.CONSUME )
  public static abstract class TestComponent
  {
    private final Map<String, String> _props;

    TestComponent( @PerInstance final Map<String, String> props )
    {
      _props = props;
    }

    @PostConstruct
    final void postConstruct()
    {
      assertNotNull( _props );
    }

  }

  @Singleton
  @Component
  interface TestDaggerComponent
    extends NoInjectionsIntegrationTest_TestComponentDaggerComponentExtension
  {
  }

  @Test
  public void scenario()
    throws Throwable
  {
    final TestDaggerComponent dagger = DaggerNoInjectionsIntegrationTest_TestDaggerComponent.create();
    final HashMap<String, String> props = new HashMap<>();
    final TestComponent component = dagger.getTestComponentDaggerSubcomponent().createFactory().create( props );
    assertEquals( component._props, props );
  }
}
