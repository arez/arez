package arez.integration.dagger.nobind;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;

public class EnhanceConsumeFactoryIntegrationTest
  extends AbstractArezIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  static abstract class BaseComponent
  {
    @Inject
    MyDependency _myDependency;
  }

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.CONSUME )
  static abstract class MyComponent
    extends BaseComponent
  {
    MyComponent( @PerInstance int value )
    {
    }

    @PostConstruct
    final void postConstruct()
    {
    }
  }

  @Singleton
  @Component
  interface TestDaggerComponent
    extends EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension
  {
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent dagger = DaggerEnhanceConsumeFactoryIntegrationTest_TestDaggerComponent.create();
    assertInvariant( () -> dagger.getMyComponentDaggerSubcomponent().createFactory().create( 22 ),
                     "Attempted to create an instance of the Arez component named 'MyComponent' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
  }
}
