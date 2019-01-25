package arez.integration.dagger.codegen;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.InjectMode;
import arez.annotations.PerInstance;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.testng.annotations.Test;

public class ReactComponentIntegrationTest
  extends AbstractCodegenIntegrationTest
{
  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  static abstract class Component
  {
  }

  static abstract class BasicReactComponent
    extends Component
  {
    @Inject
    String someParam;
  }

  @ArezComponent( disposeTrackable = Feature.DISABLE, allowEmpty = true, inject = InjectMode.CONSUME, dagger = Feature.ENABLE )
  static abstract class React4j_BasicReactComponent
    extends BasicReactComponent
  {
    React4j_BasicReactComponent( @Nonnull @PerInstance final String someValue )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "Arez_React4j_BasicReactComponent" );
    assertClassNotPresent( "React4j_BasicReactComponentDaggerModule" );
    assertClassNotPresent( "Arez_React4j_BasicReactComponent$Enhancer" );
    assertClassPresent( "Arez_React4j_BasicReactComponent$Factory" );
    assertClassPresent( "React4j_BasicReactComponentDaggerComponentExtension" );
    assertClassPresent(
      "React4j_BasicReactComponentDaggerComponentExtension$InjectSupport" );
    assertClassNotPresent(
      "React4j_BasicReactComponentDaggerComponentExtension$DaggerModule" );
    assertClassNotPresent(
      "React4j_BasicReactComponentDaggerComponentExtension$EnhancerDaggerModule" );
    assertClassPresent(
      "React4j_BasicReactComponentDaggerComponentExtension$DaggerSubcomponent" );
  }
}
