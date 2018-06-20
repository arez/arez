package arez.integration;

import arez.integration.util.SpyEventRecorder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import javax.annotation.Nonnull;
import org.json.JSONException;
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

  protected final void assertMatchesFixture( @Nonnull final SpyEventRecorder recorder )
    throws IOException, JSONException
  {
    recorder.assertMatchesFixture( fixtureDir().resolve( getFixtureFilename() ), outputFiles() );
  }

  @Nonnull
  private String getFixtureFilename()
  {
    return getClass().getName().replace( ".", "/" ) + "." + _currentMethod + ".json";
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
