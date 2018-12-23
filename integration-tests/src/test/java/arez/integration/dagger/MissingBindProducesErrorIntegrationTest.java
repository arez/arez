package arez.integration.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Feature;
import arez.annotations.Observe;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;

public class MissingBindProducesErrorIntegrationTest
  extends AbstractArezIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( dagger = Feature.ENABLE )
  static abstract class MyComponent
  {
    @Inject
    MyDependency _dependency;

    @Observe( depType = DepType.AREZ_OR_NONE )
    void run()
    {
    }
  }

  @Singleton
  @Component( modules = MissingBindProducesErrorIntegrationTest_MyComponentDaggerComponentExtension.DaggerModule.class )
  interface TestDaggerComponent
    extends MissingBindProducesErrorIntegrationTest_MyComponentDaggerComponentExtension
  {
    MyComponent component1();

    static TestDaggerComponent create()
    {
      return DaggerMissingBindProducesErrorIntegrationTest_TestDaggerComponent.create();
    }
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent daggerComponent = TestDaggerComponent.create();
    assertInvariant( daggerComponent::component1,
                     "Attempted to create an instance of the Arez component named 'MyComponent' before the dependency injection provider has been initialized. Please see the documentation at https://arez.github.io/docs/dependency_injection.html for directions how to configure dependency injection." );
  }
}
