package arez.integration.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentDependency;
import arez.annotations.Feature;
import arez.component.DisposeNotifier;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class DaggerCodeGenTest
  extends AbstractArezIntegrationTest
{
  protected final void assertClassNotPresent( @Nonnull final String className )
  {
    assertThrows( ClassNotFoundException.class, () -> Class.forName( getPackagePrefix() + className ) );
  }

  protected final void assertClassPresent( @Nonnull final String className )
    throws ClassNotFoundException
  {
    assertNotNull( Class.forName( getPackagePrefix() + className ) );
  }

  @Nonnull
  private String getPackagePrefix()
  {
    return getClass().getCanonicalName() + "_";
  }

  static class MyDependency
  {
    @Inject
    MyDependency()
    {
    }
  }

  @ArezComponent( dagger = Feature.ENABLE )
  public static abstract class MyComponent
  {
    MyComponent( @SuppressWarnings( "unused" ) MyDependency myDependency )
    {
    }

    @ComponentDependency
    DisposeNotifier myDependency()
    {
      return null;
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "Arez_MyComponent" );
    assertClassPresent( "MyComponentDaggerModule" );
    assertClassNotPresent( "Arez_MyDependency" );
    assertClassNotPresent( "MyDependencyDaggerModule" );
  }
}
