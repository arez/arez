package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Memoize;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class MemoizeProvideMethodInjectTest
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

    @Memoize
    int calc()
    {
      return 0;
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "MemoizeProvideMethodInjectTest_Arez_MyComponent" );
    assertClassPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerModule" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MemoizeProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
