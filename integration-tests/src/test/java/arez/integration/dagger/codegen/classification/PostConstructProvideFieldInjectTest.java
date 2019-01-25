package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class PostConstructProvideFieldInjectTest
  extends AbstractCodegenIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( inject = InjectMode.PROVIDE, allowEmpty = true )
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
    assertClassPresent( "PostConstructProvideFieldInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "PostConstructProvideFieldInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "PostConstructProvideFieldInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "PostConstructProvideFieldInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "PostConstructProvideFieldInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "PostConstructProvideFieldInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "PostConstructProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "PostConstructProvideFieldInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "PostConstructProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
