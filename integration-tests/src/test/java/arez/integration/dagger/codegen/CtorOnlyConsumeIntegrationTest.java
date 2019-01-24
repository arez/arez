package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class CtorOnlyConsumeIntegrationTest
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
    MyComponent( final MyDependency myDependency )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "CtorOnlyConsumeIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "CtorOnlyConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
