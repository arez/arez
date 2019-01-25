package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.PostConstruct;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class ObserveProvideFieldInjectTest
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
    MyDependency _myDependency;

    @Observe
    void autorun()
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "ObserveProvideFieldInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "ObserveProvideFieldInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "ObserveProvideFieldInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "ObserveProvideFieldInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "ObserveProvideFieldInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "ObserveProvideFieldInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "ObserveProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "ObserveProvideFieldInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "ObserveProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
