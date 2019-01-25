package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Memoize;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class MemoizeKeepAliveProvideMethodInjectTest
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

    @Memoize( keepAlive = true )
    int calc()
    {
      return 0;
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "MemoizeKeepAliveProvideMethodInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "MemoizeKeepAliveProvideMethodInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "MemoizeKeepAliveProvideMethodInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent(
      "MemoizeKeepAliveProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
