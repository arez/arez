package arez.downstream;

import gir.Gir;
import gir.GirException;
import gir.git.Git;
import gir.io.Exec;
import gir.io.FileUtil;
import gir.maven.Maven;
import gir.ruby.Buildr;
import gir.ruby.Ruby;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

public final class CollectBuildStats
{
  public static void main( final String[] args )
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
      final String version = WorkspaceUtil.getVersion();
      final OrderedProperties overallStatistics = new OrderedProperties();
      final Path workingDirectory = WorkspaceUtil.setupWorkingDirectory();

      final Path path = WorkspaceUtil.getFixtureDirectory().resolve( "statistics.properties" );
      final OrderedProperties fixtureStatistics = new OrderedProperties();
      fixtureStatistics.load( Files.newBufferedReader( path ) );

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
          Stream.of( "raw",
                     "arez",
                     "dagger",
                     "raw_maven",
                     "arez_maven",
                     "dagger_maven",
                     "raw_maven_j2cl",
                     "arez_maven_j2cl",
                     "dagger_maven_j2cl" ).forEach( branch -> {
            Gir.messenger().info( "Processing branch " + branch + "." );

            final boolean isMaven = branch.contains( "_maven" );
            final boolean isj2cl = branch.contains( "_j2cl" );

            Git.checkout( branch );
            Git.clean();

            Gir.messenger().info( "Building branch " + branch + " prior to modifications." );
            boolean initialBuildSuccess = false;
            try
            {
              if ( isMaven )
              {
                WorkspaceUtil.customizeMaven( appDirectory );
              }
              else
              {
                WorkspaceUtil.customizeBuildr( appDirectory );
              }

              final String prefix = branch + ".before";
              final Path archiveDir = WorkspaceUtil.getArchiveDir( workingDirectory, prefix );
              buildAndRecordStatistics( archiveDir, !isMaven, isj2cl );
              WorkspaceUtil.loadStatistics( overallStatistics, archiveDir, prefix );
              initialBuildSuccess = true;
            }
            catch ( final GirException | IOException e )
            {
              Gir.messenger().info( "Failed to build branch '" + branch + "' before modifications.", e );
            }

            Git.resetBranch();
            Git.clean();

            final String newBranch = branch + "-ArezUpgrade-" + version;
            if ( Git.remoteTrackingBranches().contains( "origin/" + newBranch ) )
            {
              Git.checkout( newBranch );
              Git.resetBranch( "origin/" + newBranch );
            }
            else
            {
              Git.checkout( branch );
              Git.clean();
              Git.checkout( newBranch, true );
            }

            if ( isMaven )
            {
              Maven.patchPomProperty( appDirectory,
                                      () -> "Update the 'arez' dependencies to version '" + version + "'",
                                      "arez.version",
                                      version );
            }
            else
            {
              Buildr.patchBuildYmlDependency( appDirectory, "org.realityforge.arez", version );
            }

            Gir.messenger().info( "Building branch " + branch + " after modifications." );
            if ( isMaven )
            {
              WorkspaceUtil.customizeMaven( appDirectory );
            }
            else
            {
              WorkspaceUtil.customizeBuildr( appDirectory );
            }

            final String prefix = branch + ".after";
            final Path archiveDir = WorkspaceUtil.getArchiveDir( workingDirectory, prefix );
            try
            {
              buildAndRecordStatistics( archiveDir, !isMaven, isj2cl );
              WorkspaceUtil.loadStatistics( overallStatistics, archiveDir, prefix );
              if ( !isMaven || isj2cl )
              {
                WorkspaceUtil.loadStatistics( fixtureStatistics, archiveDir, version + "." + branch );
              }
              if ( isMaven )
              {
                // Reset is required to remove changes that were made to the pom to add local repository
                Git.resetBranch();
              }
              Git.checkout( branch );
              Exec.system( "git", "merge", newBranch );
              Git.deleteBranch( newBranch );
            }
            catch ( final GirException | IOException e )
            {
              if ( !initialBuildSuccess )
              {
                Gir.messenger().error( "Failed to build branch '" + branch + "' before modifications " +
                                       "but branch also failed prior to modifications.", e );
              }
              else
              {
                Gir.messenger().error( "Failed to build branch '" + branch + "' after modifications.", e );
              }
              FileUtil.deleteDir( archiveDir );
              /*
               * If the build has failed for one of the downstream projects then make sure the command fails.
               */
              if ( e instanceof GirException )
              {
                //noinspection RedundantCast
                throw (GirException) e;
              }
              else
              {
                throw new GirException( e );
              }
            }
          } );
        } );
      } );

      overallStatistics.keySet().forEach( k -> System.out.println( k + ": " + overallStatistics.get( k ) ) );
      final Path statisticsFile = workingDirectory.resolve( "statistics.properties" );
      Gir.messenger().info( "Writing overall build statistics to " + statisticsFile + "." );
      WorkspaceUtil.writeProperties( statisticsFile, overallStatistics );

      if ( WorkspaceUtil.storeStatistics() )
      {
        Gir.messenger().info( "Updating fixture build statistics at" + path + "." );
        WorkspaceUtil.writeProperties( path, fixtureStatistics );
      }
    } );
  }

  private static void buildAndRecordStatistics( @Nonnull final Path archiveDir,
                                                final boolean useBuildr,
                                                final boolean isj2cl )
  {
    if ( !archiveDir.toFile().mkdirs() )
    {
      final String message = "Error creating archive directory: " + archiveDir;
      Gir.messenger().error( message );
    }

    if ( useBuildr )
    {
      // Perform the build
      Ruby.buildr( "clean", "package", "EXCLUDE_GWT_DEV_MODULE=true", "GWT=react4j-todomvc" );

      archiveBuildrOutput( archiveDir );
    }
    else if ( isj2cl )
    {
      // Assume maven
      Exec.system( "mvn", "clean", "package", "-Pdevmode" );

      archivej2clOutput( archiveDir );
    }
    else
    {
      // Assume maven
      Exec.system( "mvn", "clean", "package" );

      archiveMavenOutput( archiveDir );
    }
    archiveStatistics( archiveDir );
  }

  private static void archiveStatistics( @Nonnull final Path archiveDir )
  {
    final OrderedProperties properties = new OrderedProperties();
    properties.setProperty( "todomvc.size", String.valueOf( getTodoMvcSize( archiveDir ) ) );
    final long todoMvcGzSize = getTodoMvcGzSize( archiveDir );
    if ( 0 != todoMvcGzSize )
    {
      //j2cl does not produce a gzipped version so no need to include 0 all the time
      properties.setProperty( "todomvc.gz.size", String.valueOf( todoMvcGzSize ) );
    }

    final Path statisticsFile = archiveDir.resolve( "statistics.properties" );
    Gir.messenger().info( "Archiving statistics to " + statisticsFile + "." );
    WorkspaceUtil.writeProperties( statisticsFile, properties );
  }

  private static void archiveBuildrOutput( @Nonnull final Path archiveDir )
  {
    final Path currentDirectory = FileUtil.getCurrentDirectory();
    WorkspaceUtil.archiveDirectory( currentDirectory.resolve( "target/assets" ), archiveDir.resolve( "assets" ) );
    WorkspaceUtil.archiveDirectory( currentDirectory.resolve( "target/gwt_compile_reports/react4j.todomvc.TodomvcProd" ),
                                    archiveDir.resolve( "compileReports" ) );
  }

  private static void archivej2clOutput( @Nonnull final Path archiveDir )
  {
    WorkspaceUtil.archiveDirectory( FileUtil.getCurrentDirectory().resolve( "out/sources" ),
                                    archiveDir.resolve( "sources" ) );
    WorkspaceUtil.archiveFile( FileUtil.getCurrentDirectory().resolve( "out/app.js" ),
                               archiveDir.resolve( "assets/todomvc/todomvc.nocache.js" ) );
    WorkspaceUtil.archiveFile( FileUtil.getCurrentDirectory().resolve( "out/app.map" ),
                               archiveDir.resolve( "assets/todomvc/todomvc.nocache.map" ) );
  }

  private static void archiveMavenOutput( @Nonnull final Path archiveDir )
  {
    WorkspaceUtil.archiveDirectory( FileUtil.getCurrentDirectory().resolve( "target/react4j-todomvc-1.0.0-SNAPSHOT" ),
                                    archiveDir.resolve( "assets" ) );
    WorkspaceUtil.archiveDirectory( FileUtil.getCurrentDirectory().resolve( "target/extras" ),
                                    archiveDir.resolve( "compileReports" ) );
  }

  private static long getTodoMvcSize( @Nonnull final Path archiveDir )
  {
    return WorkspaceUtil.getFileSize( archiveDir.resolve( "assets" )
                                        .resolve( "todomvc" )
                                        .resolve( "todomvc.nocache.js" ) );
  }

  private static long getTodoMvcGzSize( @Nonnull final Path archiveDir )
  {
    return WorkspaceUtil.getFileSize( archiveDir.resolve( "assets" )
                                        .resolve( "todomvc" )
                                        .resolve( "todomvc.nocache.js.gz" ) );
  }
}
