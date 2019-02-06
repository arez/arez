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

public class SimpleConsumeIntegrationTest
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
  }

  @Singleton
  @Component
  interface TestDaggerComponent
  {
    SimpleConsumeIntegrationTest_Arez_MyComponent component();
  }

  @Test
  public void scenario()
  {
    assertNotNull( DaggerSimpleConsumeIntegrationTest_TestDaggerComponent.create().component()._myDependency );
  }
}