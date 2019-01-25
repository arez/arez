package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.InjectMode;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class TrackProvideFieldInjectTest
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
    assertClassPresent( "TrackProvideFieldInjectTest_Arez_MyComponent" );
    assertClassPresent( "TrackProvideFieldInjectTest_MyComponentDaggerModule" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_Arez_MyComponent$Factory" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "TrackProvideFieldInjectTest_MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "TrackProvideFieldInjectTest_MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
