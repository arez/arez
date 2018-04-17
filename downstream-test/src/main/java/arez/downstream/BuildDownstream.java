package arez.downstream;

import gir.Gir;
import gir.GirException;
import gir.delta.Patch;
import gir.git.Git;
import gir.io.Exec;
import gir.io.FileUtil;
import gir.ruby.Buildr;
import gir.ruby.Ruby;
import gir.sys.SystemProperty;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

@SuppressWarnings( "Duplicates" )
public final class BuildDownstream
{
  public static void main( final String[] args )
    throws Exception
  {
    try
    {
      run();
    }
    catch ( final Exception e )
    {
      System.err.println( "Failed command." );
      e.printStackTrace( System.err );
      System.exit( 42 );
    }
  }

  private static void run()
    throws Exception
  {
    Gir.go( () -> {
      final String version = SystemProperty.get( "arez.version" );
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
      Stream.of( "arez-browserlocation",
                 "arez-promise",
                 "arez-networkstatus",
                 "arez-idlestatus",
                 "arez-spytools",
                 "arez-ticker",
                 "arez-timeddisposer" )
        .forEach( project -> FileUtil.inDirectory( workingDirectory, () -> {
          Gir.messenger().info( "Cloning " + project + " into " + workingDirectory );
          Git.clone( "https://github.com/arez/" + project + ".git", project );
          final Path appDirectory = workingDirectory.resolve( project );
          FileUtil.inDirectory( appDirectory, () -> {
            Git.fetch();
            Git.resetBranch();
            Git.checkout();
            Git.pull();
            Git.deleteLocalBranches();
            Gir.messenger().info( "Processing branch master." );

            Git.checkout( "master" );
            Git.clean();
            final String newBranch = "master-ArezUpgrade-" + version;

            Git.checkout( newBranch, true );
            if ( Git.remoteTrackingBranches().contains( "origin/" + newBranch ) )
            {
              Git.pull();
            }
            Git.clean();

            Gir.messenger().info( "Building branch master prior to modifications." );
            boolean initialBuildSuccess = false;
            try
            {
              customizeBuildr( appDirectory, localRepositoryUrl );
              Ruby.buildr( "clean", "package", "PRODUCT_VERSION=", "PREVIOUS_PRODUCT_VERSION=" );
              initialBuildSuccess = true;
            }
            catch ( final GirException e )
            {
              Gir.messenger().info( "Failed to build branch 'master' before modifications.", e );
            }

            Git.resetBranch();
            Git.clean();

            final String group = "org.realityforge.arez";
            final Function<String, String> patchFunction1 = c -> Buildr.patchMavenCoordinates( c, group, version );
            final boolean patched =
              Patch.patchAndAddFile( appDirectory, appDirectory.resolve( "build.yaml" ), patchFunction1 );

            if ( patched )
            {
              final String message = "Update the '" + group + "' dependencies to version '" + version + "'";
              final Function<String, String> patchFunction = c -> c.replace( "### Unreleased\n\n",
                                                                             "### Unreleased\n\n" + message + "\n\n" );
              Patch.patchAndAddFile( appDirectory, appDirectory.resolve( "CHANGELOG.md" ), patchFunction );
              Git.commit( message );

              Gir.messenger().info( "Building branch master after modifications." );
              customizeBuildr( appDirectory, localRepositoryUrl );

              try
              {
                Ruby.buildr( "perform_release",
                             "LAST_STAGE=PatchChangelogPostRelease",
                             "PRODUCT_VERSION=",
                             "PREVIOUS_PRODUCT_VERSION=" );
                Git.checkout( "master" );
                Exec.system( "git", "merge", newBranch );
                Git.deleteBranch( newBranch );
              }
              catch ( final GirException e )
              {
                if ( !initialBuildSuccess )
                {
                  Gir.messenger().error( "Failed to build branch 'master' before modifications " +
                                         "but branch also failed prior to modifications.", e );
                }
                else
                {
                  Gir.messenger().error( "Failed to build branch 'master' after modifications.", e );
                }
                throw e;
              }
            }
            else
            {
              Gir.messenger().info( "Branch master not rebuilt as no modifications made." );
              Git.checkout( "master" );
              Git.deleteBranch( newBranch );
            }
          } );
        } ) );
    } );
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
}
