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
    assertClassPresent( "Arez_MyComponent" );
    assertClassPresent( "MyComponentDaggerModule" );
    assertClassNotPresent( "Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "Arez_MyComponent$Factory" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
