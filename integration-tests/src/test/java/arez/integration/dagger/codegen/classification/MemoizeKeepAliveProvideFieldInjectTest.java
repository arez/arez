package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.InjectMode;
import arez.annotations.Memoize;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class MemoizeKeepAliveProvideFieldInjectTest
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
    assertClassPresent( "MemoizeKeepAliveProvideFieldInjectTest_Arez_MyComponent" );
    assertClassNotPresent( "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerModule" );
    assertClassPresent( "MemoizeKeepAliveProvideFieldInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "MemoizeKeepAliveProvideFieldInjectTest_Arez_MyComponent$Factory" );
    assertClassPresent( "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerComponentExtension" );
    assertClassPresent( "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent(
      "MemoizeKeepAliveProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
