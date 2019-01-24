package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class CtorOnlyConsumeFactoryIntegrationTest
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
    MyComponent( @PerInstance int value, final MyDependency myDependency )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "CtorOnlyConsumeFactoryIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "CtorOnlyConsumeFactoryIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassPresent( "CtorOnlyConsumeFactoryIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent( "CtorOnlyConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
