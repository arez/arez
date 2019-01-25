package arez.integration.dagger.codegen;

import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

public class AbstractCodegenIntegrationTest
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
}
