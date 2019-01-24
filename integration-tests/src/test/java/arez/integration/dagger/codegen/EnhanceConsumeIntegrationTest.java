package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class EnhanceConsumeIntegrationTest
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

    @PostConstruct
    final void postConstruct()
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "EnhanceConsumeIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerModule" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "EnhanceConsumeIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent( "EnhanceConsumeIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
