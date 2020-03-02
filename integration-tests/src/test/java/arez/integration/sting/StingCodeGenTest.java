package arez.integration.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import sting.Injectable;
import static org.testng.Assert.*;

public class StingCodeGenTest
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

  @Injectable
  static class MyDependency
  {
    MyDependency()
    {
    }
  }

  @ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
  public static abstract class MyComponent
  {
    MyComponent( @SuppressWarnings( "unused" ) MyDependency myDependency )
    {
    }
  }

  @Test
  public void scenario()
    throws Exception
  {
    assertClassPresent( "Arez_MyComponent" );
    assertClassPresent( "MyComponentFragment" );
    assertClassNotPresent( "Arez_MyDependency" );
    assertClassNotPresent( "MyDependencyFragment" );
  }
}
