package arez.integration.dagger.inherit;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SimpleConsumeFactoryIntegrationTest
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
  }

  @Singleton
  @Component
  interface TestDaggerComponent
    extends SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension
  {
    SimpleConsumeFactoryIntegrationTest_Arez_MyComponent.Factory factory();
  }

  @Test
  public void scenario()
  {
    final TestDaggerComponent dagger = DaggerSimpleConsumeFactoryIntegrationTest_TestDaggerComponent.create();
    dagger.bindMyComponent();
    assertNotNull( dagger.factory().create( 2 )._myDependency );
  }
}
