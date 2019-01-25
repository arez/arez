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
    assertClassPresent( "Arez_MyComponent" );
    assertClassNotPresent( "MyComponentDaggerModule" );
    assertClassPresent( "Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "Arez_MyComponent$Factory" );
    assertClassPresent( "MyComponentDaggerComponentExtension" );
    assertClassPresent( "MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent( "MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
