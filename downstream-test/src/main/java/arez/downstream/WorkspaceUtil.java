package arez.downstream;

import gir.Gir;
import gir.GirException;
import gir.delta.Patch;
import gir.git.Git;
import gir.io.FileUtil;
import gir.sys.SystemProperty;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

final class WorkspaceUtil
{
  private WorkspaceUtil()
  {
  }

  static boolean storeStatistics()
  {
    return System.getProperty( "arez.deploy_test.store_statistics", "false" ).equals( "true" );
  }

  static boolean buildBeforeChanges()
  {
    return System.getProperty( "arez.deploy_test.build_before", "true" ).equals( "false" );
  }

  @Nonnull
  static String getVersion()
  {
    return SystemProperty.get( "arez.next.version" );
  }

  @Nonnull
  static Path getFixtureDirectory()
  {
    return Paths.get( SystemProperty.get( "arez.deploy_test.fixture_dir" ) ).toAbsolutePath().normalize();
  }

  @Nonnull
  private static String getLocalRepositoryUrl()
  {
    return SystemProperty.get( "arez.deploy_test.local_repository_url" );
  }

  @Nonnull
  static Path setupWorkingDirectory()
  {
    final Path workingDirectory =
      Paths.get( SystemProperty.get( "arez.deploy_test.work_dir" ) ).toAbsolutePath().normalize();
    if ( !workingDirectory.toFile().exists() )
    {
      Gir.messenger().info( "Working directory does not exist." );
      Gir.messenger().info( "Creating directory: " + workingDirectory );
      if ( !workingDirectory.toFile().mkdirs() )
      {
        Gir.messenger().error( "Failed to create working directory: " + workingDirectory );
      }
    }
    return workingDirectory;
  }

  static void archiveFile( @Nonnull final Path sourceFilename, @Nonnull final Path targetFilename )
  {
    Gir.messenger().info( "Archiving output file " + sourceFilename + " to " + targetFilename );
    try
    {
      final Path dir = targetFilename.getParent();
      if ( !dir.toFile().exists() )
      {
        if ( !dir.toFile().mkdirs() )
        {
          Gir.messenger().error( "Failed to create archive directory: " + dir );
        }
      }
      Files.copy( sourceFilename, targetFilename );
    }
    catch ( final IOException e )
    {
      final String message = "Failed to archive file: " + sourceFilename;
      Gir.messenger().error( message, e );
    }
  }

  static void archiveDirectory( @Nonnull final Path assetsDir, @Nonnull final Path targetDir )
  {
    Gir.messenger().info( "Archiving output " + assetsDir + " to " + targetDir );
    try
    {
      FileUtil.copyDirectory( assetsDir, targetDir );
    }
    catch ( final GirException e )
    {
      final String message = "Failed to archive directory: " + assetsDir;
      Gir.messenger().error( message, e );
    }
  }

  static long getFileSize( @Nonnull final Path path )
  {
    final File file = path.toFile();
    assert file.exists();
    return file.length();
  }

  static void writeProperties( @Nonnull final Path outputFile, @Nonnull final OrderedProperties properties )
  {
    try
    {
      properties.store( new FileWriter( outputFile.toFile() ), "" );
      Patch.file( outputFile, c -> c.replaceAll( "#.*\n", "" ) );
    }
    catch ( final IOException ioe )
    {
      final String message = "Failed to write properties file: " + outputFile;
      Gir.messenger().error( message, ioe );
      throw new GirException( message, ioe );
    }
  }

  static void loadStatistics( @Nonnull final OrderedProperties statistics,
                              @Nonnull final Path archiveDir,
                              @Nonnull final String keyPrefix )
    throws IOException
  {
    final Properties properties = new Properties();
    properties.load( Files.newBufferedReader( archiveDir.resolve( "statistics.properties" ) ) );
    properties.forEach( ( key, value ) -> statistics.put( keyPrefix + "." + key, value ) );
  }

  @Nonnull
  static Path getArchiveDir( @Nonnull final Path workingDirectory, @Nonnull final String build )
  {
    return workingDirectory.resolve( "archive" ).resolve( build );
  }

  static void customizeMaven( @Nonnull final Path appDirectory )
  {
    customizeMaven( appDirectory, getLocalRepositoryUrl() );
  }

  private static void customizeMaven( @Nonnull final Path appDirectory, @Nonnull final String localRepositoryUrl )
  {
    final String replacement =
      "<repositories>\n" +
      "    <repository>\n" +
      "      <id>local-repository</id>\n" +
      "      <url>" + localRepositoryUrl + "</url>\n" +
      "    </repository>";
    if ( !Patch.file( appDirectory.resolve( "pom.xml" ), c -> c.replace( "<repositories>", replacement ) ) )
    {
      Gir.messenger().error( "Failed to patch pom.xml to add local repository." );
    }
  }

  static void customizeBuildr( @Nonnull final Path appDirectory )
  {
    customizeBuildr( appDirectory, getLocalRepositoryUrl() );
  }

  private static void customizeBuildr( @Nonnull final Path appDirectory, @Nonnull final String localRepositoryUrl )
  {
    try
    {
      final String content = "repositories.remote.unshift('" + localRepositoryUrl + "')\n";
      Files.write( appDirectory.resolve( "_buildr.rb" ), content.getBytes() );
    }
    catch ( final IOException ioe )
    {
      Gir.messenger().error( "Failed to emit _buildr.rb configuration file.", ioe );
    }
  }

  static void forEachBranch( @Nonnull final String name,
                             @Nonnull final String repositoryUrl,
                             @Nonnull final List<String> branches,
                             @Nonnull final Consumer<BuildContext> action )
  {
    final Path workingDirectory = setupWorkingDirectory();
    FileUtil.inDirectory( workingDirectory, () -> {
      Gir.messenger().info( "Cloning " + name + " into " + workingDirectory );
      Git.clone( repositoryUrl, name );
      final Path appDirectory = workingDirectory.resolve( name );
      FileUtil.inDirectory( appDirectory, () -> {
        Git.fetch();
        Git.resetBranch();
        Git.checkout();
        Git.pull();
        Git.deleteLocalBranches();
        branches.forEach( branch -> {
          Gir.messenger().info( "Processing branch " + branch + "." );

          Git.checkout( branch );
          Git.clean();

          action.accept( new BuildContext( workingDirectory, appDirectory, branch ) );

        } );
      } );
    } );
  }

  static class BuildContext
  {
    @Nonnull
    final Path workingDirectory;
    @Nonnull
    final Path appDirectory;
    @Nonnull
    final String branch;

    BuildContext( @Nonnull final Path workingDirectory,
                  @Nonnull final Path appDirectory,
                  @Nonnull final String branch )
    {
      this.workingDirectory = Objects.requireNonNull( workingDirectory );
      this.appDirectory = Objects.requireNonNull( appDirectory );
      this.branch = Objects.requireNonNull( branch );
    }
  }
}
