package arez.apitest;

import gir.io.Exec;
import gir.sys.SystemProperty;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonArray;
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
      final JsonArray differences = Json.createReader( new FileInputStream( reportFile ) ).readArray();
      final File fixture = getFixtureReport();
      if ( !fixture.exists() )
      {
        if ( !differences.isEmpty() )
        {
          fail( "Unable to locate test fixture describing expected API changes when there is " + differences.size() +
                " differences detected. Expected fixture file to be located at " + fixture.getAbsolutePath() );
        }
      }
      else if ( storeApiDiff && differences.isEmpty() )
      {
        if ( fixture.exists() )
        {
          System.out.println( "Deleting existing fixture file as no API differences detected. Fixture: " + fixture );
          assertTrue( fixture.delete() );
        }
      }
      else
      {
        final byte[] reportData = Files.readAllBytes( reportFile.toPath() );
        final byte[] expectedData = Files.readAllBytes( fixture.toPath() );
        assertEquals( new String( reportData, Charset.forName( "UTF-8" ) ),
                      new String( expectedData, Charset.forName( "UTF-8" ) ) );
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
