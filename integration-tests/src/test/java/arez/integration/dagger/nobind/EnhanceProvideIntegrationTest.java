package arez.integration.dagger.nobind;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;

public class EnhanceProvideIntegrationTest
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

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.PROVIDE )
  public static abstract class MyComponent
    extends BaseComponent
  {
    @PostConstruct
    final void postConstruct()
    {
    }
  }

  @Singleton
  @Component( modules = EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension.DaggerModule.class )
  interface TestDaggerComponent
    extends EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension
  {
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent dagger = DaggerEnhanceProvideIntegrationTest_TestDaggerComponent.create();
    assertInvariant( () -> dagger.createMyComponentProvider().get(),
                     "Attempted to create an instance of the Arez component named 'MyComponent' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
  }
}
