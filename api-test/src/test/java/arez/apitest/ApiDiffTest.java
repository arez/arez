package arez.apitest;

import gir.io.Exec;
import gir.sys.SystemProperty;
import java.io.File;
import java.nio.file.Files;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ApiDiffTest
{
  @Test
  public void compareApi()
    throws Exception
  {

    final boolean storeApiDiff = SystemProperty.get( "arez.api_test.store_api_diff" ).equals( "true" );
    final File reportFile = storeApiDiff ? getFixtureReport() : File.createTempFile( "apidiff", ".json" );
    try
    {
      generateReport( reportFile );
      final File fixture = getFixtureReport();
      if ( !fixture.exists() )
      {
        fail( "Unable to locate test fixture at " + fixture.getAbsolutePath() );
      }
      else
      {
        final byte[] reportData = Files.readAllBytes( reportFile.toPath() );
        final byte[] expectedData = Files.readAllBytes( fixture.toPath() );
        assertEquals( reportData, expectedData );
      }
    }
    finally
    {
      if ( !storeApiDiff )
      {
        assertTrue( !reportFile.exists() || reportFile.delete() );
      }
    }
  }

  private void generateReport( @Nonnull final File reportFile )
  {
    final String oldApiLabel = "org.realityforge.arez:arez-core:jar:" + SystemProperty.get( "arez.prev.version" );
    final String oldApi = oldApiLabel + "::" + SystemProperty.get( "arez.prev.jar" );
    final String newApiLabel = "org.realityforge.arez:arez-core:jar:" + SystemProperty.get( "arez.next.version" );
    final String newApi = newApiLabel + "::" + SystemProperty.get( "arez.next.jar" );
    Exec.system( "java",
                 "-jar",
                 SystemProperty.get( "arez.revapi.jar" ),
                 "--old-api",
                 oldApi,
                 "--new-api",
                 newApi,
                 "--output-file",
                 reportFile.toString() );
  }

  @Nonnull
  private File getFixtureReport()
  {
    return new File( SystemProperty.get( "arez.api_test.fixture_dir" ),
                     SystemProperty.get( "arez.prev.version" ) + "-" +
                     SystemProperty.get( "arez.next.version" ) + ".json" );
  }
}
