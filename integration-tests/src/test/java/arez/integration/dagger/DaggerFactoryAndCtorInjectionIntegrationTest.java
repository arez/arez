package arez.integration.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DaggerFactoryAndCtorInjectionIntegrationTest
  extends AbstractArezIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.CONSUME )
  static abstract class TestComponent
  {
    private final Map<String, String> _props;
    private final MyDependency _myDependency;

    TestComponent( @PerInstance final Map<String, String> props, final MyDependency myDependency )
    {
      _props = props;
      _myDependency = myDependency;
    }

    @PostConstruct
    final void postConstruct()
    {
      assertNotNull( _props );
      assertNotNull( _myDependency );
    }

    Map<String, String> getProps()
    {
      return _props;
    }
  }

  @Singleton
  @Component
  interface TestDaggerComponent
    extends DaggerFactoryAndCtorInjectionIntegrationTest_TestComponentDaggerComponentExtension
  {
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent daggerComponent =
      DaggerDaggerFactoryAndCtorInjectionIntegrationTest_TestDaggerComponent.create();
    final HashMap<String, String> props = new HashMap<>();
    final TestComponent component =
      daggerComponent.getTestComponentDaggerSubcomponent().createFactory().create( props );
    assertEquals( component.getProps(), props );
  }
}
