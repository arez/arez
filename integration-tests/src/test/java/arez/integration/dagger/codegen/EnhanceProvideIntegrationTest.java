package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class EnhanceProvideIntegrationTest
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
    assertClassPresent( "EnhanceProvideIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerModule" );
    assertClassPresent( "EnhanceProvideIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "EnhanceProvideIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "EnhanceProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
