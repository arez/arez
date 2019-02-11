package arez.downstream;

import gir.sys.SystemProperty;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.annotation.Nonnull;
import org.realityforge.gwt.symbolmap.SoycSizeMaps;
import org.realityforge.gwt.symbolmap.SymbolEntryIndex;

@SuppressWarnings( "SameParameterValue" )
abstract class AbstractDownstreamTest
{
  @Nonnull
  final Properties loadProperties( @Nonnull final File file )
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
  final Path getFixtureDir()
  {
    return Paths
      .get( SystemProperty.get( "arez.deploy_test.fixture_dir" ) )
      .toAbsolutePath()
      .normalize();
  }

  @Nonnull
  final Path getArchiveDir()
  {
    return getWorkDir().resolve( "archive" );
  }

  @Nonnull
  final Path getWorkDir()
  {
    return Paths
      .get( SystemProperty.get( "arez.deploy_test.work_dir" ) )
      .toAbsolutePath()
      .normalize();
  }

  @Nonnull
  final SoycSizeMaps getSoycSizeMaps( @Nonnull final String moduleName, @Nonnull final String build )
    throws Exception
  {
    return SoycSizeMaps.readFromGzFile( getStoriesPath( moduleName, build ) );
  }

  @Nonnull
  private Path getStoriesPath( @Nonnull final String moduleName, @Nonnull final String build )
  {
    return getArchiveDir()
      .resolve( build )
      .resolve( "compileReports" )
      .resolve( moduleName )
      .resolve( "soycReport" )
      .resolve( "stories0.xml.gz" );
  }

  @Nonnull
  final SymbolEntryIndex getSymbolMapIndex( @Nonnull final String moduleName, @Nonnull final String build )
    throws Exception
  {
    return SymbolEntryIndex.readSymbolMapIntoIndex( getSymbolMapPath( moduleName, build ) );
  }

  @Nonnull
  final Path getSymbolMapPath( @Nonnull final String moduleName, @Nonnull final String build )
    throws IOException
  {
    final Path symbolMapsDir =
      getArchiveDir()
        .resolve( build )
        .resolve( "assets" )
        .resolve( "WEB-INF" )
        .resolve( "deploy" )
        .resolve( moduleName )
        .resolve( "symbolMaps" );

    return Files.list( symbolMapsDir ).findFirst().orElseThrow( AssertionError::new );
  }

  @Nonnull
  final Properties loadBuildStatistics()
    throws IOException
  {
    return loadProperties( getWorkDir().resolve( "statistics.properties" ).toFile() );
  }

  @Nonnull
  final Properties loadFixtureStatistics()
    throws IOException
  {
    return loadProperties( getFixtureDir().resolve( "statistics.properties" ).toFile() );
  }
}
