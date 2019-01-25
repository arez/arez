package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.PostConstruct;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class PostConstructProvideMethodInjectTest
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
    void setDep( MyDependency myDependency )
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
    assertClassPresent( "PostConstructProvideMethodInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "PostConstructProvideMethodInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "PostConstructProvideMethodInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "PostConstructProvideMethodInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "PostConstructProvideMethodInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "PostConstructProvideMethodInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "PostConstructProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "PostConstructProvideMethodInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "PostConstructProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
