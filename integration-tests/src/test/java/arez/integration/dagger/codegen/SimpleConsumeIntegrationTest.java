package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class SimpleConsumeIntegrationTest
  extends AbstractCodegenIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.CONSUME )
  static abstract class MyComponent
  {
    @Inject
    MyDependency _myDependency;
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "SimpleConsumeIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "SimpleConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
