package arez.downstream;

import gir.Gir;
import gir.GirException;
import gir.git.Git;
import gir.io.FileUtil;
import gir.ruby.Buildr;
import gir.ruby.Ruby;
import gir.sys.SystemProperty;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

public final class CollectBuildStats
{
  public static void main( final String[] args )
    throws Exception
  {
    Gir.go( () -> {
      final String version = SystemProperty.get( "arez.version" );
      final OrderedProperties statistics = new OrderedProperties();
      final Path workingDirectory =
        Paths.get( SystemProperty.get( "arez.deploy_test.work_dir" ) ).toAbsolutePath().normalize();
      final String localRepositoryUrl = SystemProperty.get( "arez.deploy_test.local_repository_url" );

      if ( !workingDirectory.toFile().exists() )
      {
        Gir.messenger().info( "Working directory does not exist." );
        Gir.messenger().info( "Creating directory: " + workingDirectory );
        if ( !workingDirectory.toFile().mkdirs() )
        {
          Gir.messenger().error( "Failed to create working directory: " + workingDirectory );
        }
      }

      FileUtil.inDirectory( workingDirectory, () -> {
        Gir.messenger().info( "Cloning react4j-todomvc into " + workingDirectory );
        Git.clone( "https://github.com/react4j/react4j-todomvc.git", "react4j-todomvc" );
        final Path appDirectory = workingDirectory.resolve( "react4j-todomvc" );
        FileUtil.inDirectory( appDirectory, () -> {
          Git.fetch();
          Git.resetBranch();
          Git.checkout();
          Git.pull();
          Git.deleteLocalBranches();
          Stream.of( "raw", "arez", "dagger" ).forEach( branch -> {
            Gir.messenger().info( "Processing branch " + branch + "." );

            Git.checkout( branch );
            Git.pull();
            Git.clean();
            final String newBranch = branch + "-ArezUpgrade-" + version;
            System.out.println( "Checking out " + newBranch );
            Git.checkout( newBranch, true );
            if ( Git.remoteTrackingBranches().contains( "origin/" + newBranch ) )
            {
              Git.pull();
            }
            Git.clean();

            Gir.messenger().info( "Building branch " + branch + " prior to modifications." );
            boolean initialBuildSuccess = false;
            try
            {
              final String prefix = branch + ".before.";
              buildAndRecordStatistics().forEach( ( key, value ) -> statistics.put( prefix + key, value ) );
              initialBuildSuccess = true;
            }
            catch ( final GirException ge )
            {
              Gir.messenger().info( "Failed to build branch '" + branch + "' before modifications.", ge );
            }

            Git.clean();

            if ( Buildr.patchBuildYmlDependency( appDirectory, "org.realityforge.arez", version ) )
            {
              Gir.messenger().info( "Building branch " + branch + " after modifications." );
              try
              {
                final String content = "repositories.remote << '" + localRepositoryUrl + "'\n";
                Files.write( appDirectory.resolve( "_buildr.rb" ), content.getBytes() );
              }
              catch ( final IOException ioe )
              {
                Gir.messenger().error( "Failed to emit _buildr.rb configuration file.", ioe );
              }

              try
              {
                final String prefix = branch + ".after.";
                buildAndRecordStatistics().forEach( ( key, value ) -> statistics.put( prefix + key, value ) );
              }
              catch ( final GirException ge )
              {
                if ( !initialBuildSuccess )
                {
                  Gir.messenger().error( "Failed to build branch '" + branch + "' before modifications " +
                                         "but branch also failed prior to modifications.", ge );
                }
                else
                {
                  Gir.messenger().error( "Failed to build branch '" + branch + "' after modifications.", ge );
                }
              }
            }
            else
            {
              Gir.messenger().info( "Branch " + branch + " not rebuilt as no modifications made." );
            }
          } );
        } );
      } );

      statistics.forEach( ( k, v ) -> System.out.println( k + ": " + v ) );
    } );
  }

  @Nonnull
  private static OrderedProperties buildAndRecordStatistics()
  {
    Ruby.buildr( "clean", "package", "EXCLUDE_GWT_DEV_MODULE=true", "GWT=react4j-todomvc" );
    return recordStatistics();
  }

  @Nonnull
  private static OrderedProperties recordStatistics()
  {
    final OrderedProperties branchStatistics = new OrderedProperties();
    final Path currentDirectory = FileUtil.getCurrentDirectory();
    final Path outputJsFile =
      currentDirectory.resolve( "target/generated/gwt/react4j.todomvc.TodomvcProd/todomvc/todomvc.nocache.js" );
    final File jsFile = outputJsFile.toFile();
    assert jsFile.exists();
    final long length = jsFile.length();
    branchStatistics.setProperty( "todomvc.size", String.valueOf( length ) );
    return branchStatistics;
  }
}
