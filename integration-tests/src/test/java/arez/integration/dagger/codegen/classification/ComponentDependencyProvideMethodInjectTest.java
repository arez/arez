package arez.integration.dagger.codegen.classification;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.InjectMode;
import arez.component.DisposeNotifier;
import arez.integration.dagger.codegen.AbstractCodegenIntegrationTest;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class ComponentDependencyProvideMethodInjectTest
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
    void setDep( @SuppressWarnings( "unused" ) MyDependency myDependency )
    {
    }

    @ComponentDependency
    final DisposeNotifier myDependency()
    {
      return null;
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "Arez_MyComponent" );
    assertClassNotPresent( "MyComponentDaggerModule" );
    assertClassPresent( "Arez_MyComponent$Enhancer" );
    assertClassNotPresent( "Arez_MyComponent$Factory" );
    assertClassPresent( "MyComponentDaggerComponentExtension" );
    assertClassPresent( "MyComponentDaggerComponentExtension$InjectSupport" );
    assertClassPresent( "MyComponentDaggerComponentExtension$DaggerModule" );
    assertClassPresent(
      "MyComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassNotPresent( "MyComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
