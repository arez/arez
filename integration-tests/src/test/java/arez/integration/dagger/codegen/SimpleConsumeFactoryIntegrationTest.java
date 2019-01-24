package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import arez.annotations.PostConstruct;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class SimpleConsumeFactoryIntegrationTest
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
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "SimpleConsumeFactoryIntegrationTest_Arez_MyComponent" );
    assertClassNotPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "SimpleConsumeFactoryIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassPresent( "SimpleConsumeFactoryIntegrationTest_Arez_MyComponent$Factory" );
    assertClassPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent( "SimpleConsumeFactoryIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
