package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import org.testng.annotations.Test;

public class NoInjectionsConsumeFactoryIntegrationTest
  extends AbstractCodegenIntegrationTest
{
  @ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, inject = InjectMode.CONSUME )
  static abstract class MyComponent
  {
    MyComponent( @PerInstance int value )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "NoInjectionsConsumeFactoryIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "NoInjectionsConsumeFactoryIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassPresent( "NoInjectionsConsumeFactoryIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent(
      "NoInjectionsConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
