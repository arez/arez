package org.realityforge.arez.integration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonObject;
import org.json.JSONException;
import org.realityforge.arez.AbstractArezIntegrationTest;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.*;

public abstract class AbstractIntegrationTest
  extends AbstractArezIntegrationTest
{
  private String _currentMethod;

  @BeforeMethod
  public void handleTestMethodName( Method method )
  {
    _currentMethod = method.getName();
  }

  protected final void assertEqualsFixture( @Nonnull final String json )
    throws IOException, JSONException
  {
    final Path file = fixtureDir().resolve( getFixtureFilename() );
    saveIfOutputEnabled( file, json );
    final JSONComparator comparator =
      new CustomComparator( JSONCompareMode.STRICT, new Customization( "**.duration", ( o1, o2 ) -> true ) );
    JSONAssert.assertEquals( loadFile( file ), json, comparator );
  }

  @Nonnull
  protected final String getFixtureFilename()
  {
    return getClass().getName().replace( ".", "/" ) + "." + _currentMethod + ".json";
  }

  @Nonnull
  protected final String loadFile( @Nonnull final Path file )
    throws IOException
  {
    return Files.readAllLines( file ).stream().collect( Collectors.joining( "\n" ) );
  }

  protected final void saveIfOutputEnabled( @Nonnull final Path file, @Nonnull final String contents )
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

  protected final void record( @Nonnull final SpyEventRecorder recorder,
                               @Nonnull final String key,
                               @Nonnull final Object value )
  {
    final JsonObject mark =
      Json.createObjectBuilder().add( "type", "usermark" ).add( key, String.valueOf( value ) ).build();
    recorder.getEvents().add( mark );
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
