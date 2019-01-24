package arez.integration.dagger.inherit;

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
import static org.testng.Assert.*;

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
    dagger.bindMyComponent();
    assertNotNull( dagger.getMyComponentDaggerSubcomponent().createFactory().create( 22 )._myDependency );
  }
}
