package arez.integration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractIntegrationTest
  extends AbstractArezIntegrationTest
{
  private String _currentMethod;

  @BeforeMethod
  public void handleTestMethodName( Method method )
    throws Exception
  {
    _currentMethod = method.getName();
    beforeTest();
  }

  protected final void assertEqualsFixture( @Nonnull final String json )
    throws IOException, JSONException
  {
    assertEqualsFixture( json, new DefaultComparator( JSONCompareMode.STRICT ) );
  }

  protected final void assertEqualsFixture( @Nonnull final String json, @Nonnull final JSONComparator comparator )
    throws IOException, JSONException
  {
    final Path file = fixtureDir().resolve( getFixtureFilename() );
    saveIfOutputEnabled( file, json );
    JSONAssert.assertEquals( loadFile( file ), json, comparator );
  }

  @Nonnull
  private String getFixtureFilename()
  {
    return getClass().getName().replace( ".", "/" ) + "." + _currentMethod + ".json";
  }

  @Nonnull
  private String loadFile( @Nonnull final Path file )
    throws IOException
  {
    return Files.readAllLines( file ).stream().collect( Collectors.joining( "\n" ) );
  }

  private void saveIfOutputEnabled( @Nonnull final Path file, @Nonnull final String contents )
    throws IOException
  {
    if ( outputFiles() )
    {
      final File dir = file.getParent().toFile();
      if ( !dir.exists() )
      {
        assertTrue( dir.mkdirs() );
      }
      Files.write( file, ( contents + "\n" ).getBytes() );
    }
  }

  @Nonnull
  private Path fixtureDir()
  {
    final String fixtureDir = System.getProperty( "arez.integration_fixture_dir" );
    assertNotNull( fixtureDir,
                   "Expected System.getProperty( \"arez.integration_fixture_dir\" ) to return fixture directory if arez.output_fixture_data=true" );

    return new File( fixtureDir ).toPath();
  }

  private boolean outputFiles()
  {
    return System.getProperty( "arez.output_fixture_data", "false" ).equals( "true" );
  }
}
