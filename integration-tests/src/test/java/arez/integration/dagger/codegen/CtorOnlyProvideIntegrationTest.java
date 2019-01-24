package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class CtorOnlyProvideIntegrationTest
  extends AbstractCodegenIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.PROVIDE )
  public static abstract class MyComponent
  {
    MyComponent( final MyDependency myDependency )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "CtorOnlyProvideIntegrationTest_Arez_MyComponent" );
    assertClassPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "CtorOnlyProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
