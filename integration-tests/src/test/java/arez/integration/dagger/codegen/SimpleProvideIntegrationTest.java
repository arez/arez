package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import dagger.Component;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SimpleProvideIntegrationTest
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
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "Arez_MyComponent" );
    assertClassPresent( "MyComponentDaggerModule" );
    assertClassNotPresent( "Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "Arez_MyComponent$Factory" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
