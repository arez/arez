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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class MultipleParamIntegrationTest
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
  public static abstract class TestComponent
  {
    final Map<String, String> _props;
    final int _value;
    final MyDependency _myDependency;

    TestComponent( final MyDependency myDependency, @PerInstance final Map<String, String> props, @PerInstance final int value )
    {
      _myDependency = myDependency;
      _props = props;
      _value = value;
    }

    @PostConstruct
    final void postConstruct()
    {
      assertNotNull( _props );
      assertNotNull( _myDependency );
      assertNotEquals( _value, 0 );
    }
  }

  @Singleton
  @Component
  interface TestDaggerComponent
    extends MultipleParamIntegrationTest_TestComponentDaggerComponentExtension
  {
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent dagger = DaggerMultipleParamIntegrationTest_TestDaggerComponent.create();
    final HashMap<String, String> props = new HashMap<>();
    final TestComponent component = dagger.getTestComponentDaggerSubcomponent().createFactory().create( props, 23 );
    assertEquals( component._props, props );
    assertEquals( component._value, 23 );
    assertNotNull( component._myDependency );
  }
}
