package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.Memoize;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class MemoizeProvideFieldInjectTest
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
    assertClassPresent( "MemoizeProvideFieldInjectTest_Arez_MyComponent" );
    assertClassPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerModule" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MemoizeProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
