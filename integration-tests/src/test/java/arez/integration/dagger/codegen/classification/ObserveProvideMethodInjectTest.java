package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class ObserveProvideMethodInjectTest
  extends AbstractCodegenIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( inject = InjectMode.PROVIDE )
  public static abstract class MyComponent
  {
    @Inject
    void setDep( MyDependency myDependency )
    {
    }

    @Observe
    void autorun()
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "ObserveProvideMethodInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "ObserveProvideMethodInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "ObserveProvideMethodInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "ObserveProvideMethodInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "ObserveProvideMethodInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "ObserveProvideMethodInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "ObserveProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "ObserveProvideMethodInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "ObserveProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
