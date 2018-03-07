package arez.downstream;

import gir.sys.SystemProperty;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class BuildStatsTest
{
  @Test
  public void arez()
    throws Exception
  {
    compareSizesForBranch( "arez" );
  }

  @Test
  public void dagger()
    throws Exception
  {
    compareSizesForBranch( "dagger" );
  }

  private void compareSizesForBranch( @Nonnull final String branch )
    throws IOException
  {
    final Properties buildStatistics = loadBuildStatistics();
    final Properties fixtureStatistics = loadFixtureStatistics();

    final long fixtureSize =
      extractSize( fixtureStatistics, SystemProperty.get( "arez.version" ) + "." + branch );
    final long beforeSize = extractSize( buildStatistics, branch + "." + "before" );
    final long afterSize = extractSize( buildStatistics, branch + "." + "after" );

    if ( 0 != fixtureSize )
    {
      if ( fixtureSize != afterSize )
      {
        fail( "Build size does not katch the value specified in fixtures file for branch '" + branch +
              "'. The fixture specifies the value as " + fixtureSize + " while the actual size is " +
              afterSize + ". If this is acceptable then re-run the build passing PRODUCT_VERSION=... " +
              "and STORE_BUILD_STATISTICS=true to update the fixture file." );
      }
    }
    else
    {
      if ( beforeSize != afterSize )
      {
        fail( "Build size changed after upgrading arez in branch '" + branch +
              "' from " + beforeSize + " to " + afterSize + ". If this is " +
              "acceptable then re-run the build passing PRODUCT_VERSION=... " +
              "and STORE_BUILD_STATISTICS=true to update the fixture file." );
      }
    }
  }

  @Nonnegative
  private long extractSize( @Nonnull final Properties properties,
                            @Nonnull final String prefix )
  {
    return Long.parseLong( properties.getProperty( prefix + ".todomvc.size", "0" ) );
  }

  @Nonnull
  private Properties loadBuildStatistics()
    throws IOException
  {
    return loadProperties( getBuildStatisticsFile().toFile() );
  }

  @Nonnull
  private Properties loadFixtureStatistics()
    throws IOException
  {
    return loadProperties( getFixtureStatisticsFile().toFile() );
  }

  @Nonnull
  private Properties loadProperties( @Nonnegative final File file )
    throws IOException
  {
    final Properties properties = new Properties();
    try ( final FileReader fileReader = new FileReader( file ) )
    {
      properties.load( fileReader );
    }
    return properties;
  }

  @Nonnull
  private Path getFixtureStatisticsFile()
  {
    return Paths
      .get( SystemProperty.get( "arez.deploy_test.fixture_dir" ) )
      .toAbsolutePath()
      .normalize()
      .resolve( "statistics.properties" );
  }

  @Nonnull
  private Path getBuildStatisticsFile()
  {
    return Paths
      .get( SystemProperty.get( "arez.deploy_test.work_dir" ) )
      .toAbsolutePath()
      .normalize()
      .resolve( "statistics.properties" );
  }
}
