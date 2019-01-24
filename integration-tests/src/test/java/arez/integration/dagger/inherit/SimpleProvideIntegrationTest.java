package arez.integration.dagger.inherit;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.integration.AbstractArezIntegrationTest;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SimpleProvideIntegrationTest
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
  }

  @Singleton
  @Component( modules = SimpleProvideIntegrationTest_MyComponentDaggerModule.class )
  interface TestDaggerComponent
  {
    MyComponent component();
  }

  @Test
  public void scenario()
  {
    assertNotNull( DaggerSimpleProvideIntegrationTest_TestDaggerComponent.create().component()._myDependency );
  }
}
