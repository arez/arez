package arez.integration.dagger.inherit;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
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

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true )
  public static abstract class MyComponent
  {
    private final MyDependency _myDependency;

    MyComponent( MyDependency myDependency )
    {
      _myDependency = myDependency;
    }
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
