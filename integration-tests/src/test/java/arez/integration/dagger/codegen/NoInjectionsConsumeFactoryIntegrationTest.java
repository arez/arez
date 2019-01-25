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
    assertClassPresent( "Arez_MyComponent" );
    assertClassNotPresent( "MyComponentDaggerModule" );
    assertClassNotPresent( "Arez_MyComponent$Enhancer" );
    assertClassPresent( "Arez_MyComponent$Factory" );
    assertClassPresent( "MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent(
      "MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
