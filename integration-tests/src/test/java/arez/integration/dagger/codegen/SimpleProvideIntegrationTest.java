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
    assertClassPresent( "SimpleProvideIntegrationTest_Arez_MyComponent" );
    assertClassPresent( "SimpleProvideIntegrationTest_MyComponentDaggerModule" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "SimpleProvideIntegrationTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
