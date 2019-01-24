package arez.integration.dagger.codegen;

import arez.integration.AbstractArezIntegrationTest;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

public class AbstractCodegenIntegrationTest
  extends AbstractArezIntegrationTest
{
  final void assertClassNotPresent( @Nonnull final String className )
  {
    assertThrows( ClassNotFoundException.class, () -> Class.forName( "arez.integration.dagger.codegen." + className ) );
  }

  final void assertClassPresent( @Nonnull final String className )
    throws ClassNotFoundException
  {
    assertNotNull( Class.forName( "arez.integration.dagger.codegen." + className ) );
  }
}
