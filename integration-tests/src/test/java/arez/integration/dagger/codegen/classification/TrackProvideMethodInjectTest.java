package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class TrackProvideMethodInjectTest
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

    @Observe( executor = Executor.EXTERNAL )
    void render()
    {
    }

    @OnDepsChange
    public void onRenderDepsChange()
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "TrackProvideMethodInjectTest_Arez_MyComponent" );
    assertClassPresent( "TrackProvideMethodInjectTest_MyComponentDaggerModule" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "TrackProvideMethodInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "TrackProvideMethodInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
