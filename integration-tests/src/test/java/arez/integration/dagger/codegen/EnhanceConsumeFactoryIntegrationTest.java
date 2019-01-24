package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class EnhanceConsumeFactoryIntegrationTest
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

    MyComponent( @PerInstance int value )
    {
    }

    @PostConstruct
    final void postConstruct()
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerModule" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent( "EnhanceConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
