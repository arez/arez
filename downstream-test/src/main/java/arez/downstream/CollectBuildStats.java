package arez.downstream;

import gir.Gir;
import gir.bazel.Bazel;
import gir.delta.Patch;
import gir.git.Git;
import gir.io.Exec;
import gir.io.FileUtil;
import gir.maven.Maven;
import gir.ruby.Buildr;
import gir.ruby.Ruby;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;

public final class CollectBuildStats
{
  enum BuildSystem
  {
    BUILDR,
    BAZEL,
    MAVEN
  }

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
      final List<String> baseBranches =
        Arrays.asList( "raw", "arez", "dagger", "spritz", "raw_bazel", "raw_maven", "arez_maven", "dagger_maven" );

      final ArrayList<String> branches = new ArrayList<>( baseBranches );
      if ( WorkspaceUtil.buildJ2clBuilds() )
      {
        branches.add( "raw_maven_j2cl" );
        branches.add( "arez_maven_j2cl" );
        branches.add( "dagger_maven_j2cl" );
      }

      WorkspaceUtil.forEachBranch( "react4j-todomvc",
                                   "https://github.com/react4j/react4j-todomvc.git",
                                   branches,
                                   context -> buildBranch( context, WorkspaceUtil.getVersion() ) );
      WorkspaceUtil.collectStatistics( branches, branch -> !branch.endsWith( "_maven" ), true );
    } );
  }

  private static void buildBranch( @Nonnull final WorkspaceUtil.BuildContext context, @Nonnull final String version )
  {
    final boolean isj2cl = context.branch.contains( "_j2cl" );

    final BuildSystem buildSystem =
      context.branch.contains( "_maven" ) ? BuildSystem.MAVEN :
      context.branch.contains( "_bazel" ) ? BuildSystem.BAZEL :
      BuildSystem.BUILDR;

    final boolean initialBuildSuccess = WorkspaceUtil.runBeforeBuild( context, () -> {
      final String prefix = context.branch + ".before";
      final Path archiveDir = WorkspaceUtil.getArchiveDir( context.workingDirectory, prefix );
      buildAndRecordStatistics( context.appDirectory, archiveDir, buildSystem, isj2cl );
    } );

    WorkspaceUtil.runAfterBuild( context, initialBuildSuccess, () -> {
      if ( BuildSystem.MAVEN == buildSystem )
      {
        Maven.patchPomProperty( context.appDirectory,
                                () -> "Update the 'arez' dependencies to version '" + version + "'",
                                "arez.version",
                                version );
      }
      else if ( BuildSystem.BAZEL == buildSystem )
      {
        final Path thirdpartyDir = context.appDirectory.resolve( "thirdparty" );
        Patch.patchAndAddFile( context.appDirectory,
                               thirdpartyDir.resolve( "dependencies.yml" ),
                               c -> Bazel.patchMavenCoordinates( c, "org.realityforge.arez", version ) );
        Exec.system( "bazel", "run", "//thirdparty:regenerate_depgen_extension", "--", "--reset-cached-metadata" );
        FileUtil.inDirectory( context.appDirectory,
                              () -> Git.add( thirdpartyDir.resolve( "dependencies.bzl" ).toString() ) );
        Git.commit( "Update the 'arez' dependencies to version '" + version + "'" );
      }
      else
      {
        Buildr.patchBuildYmlDependency( context.appDirectory, "org.realityforge.arez", version );
      }

      final String prefix = context.branch + ".after";
      final Path archiveDir = WorkspaceUtil.getArchiveDir( context.workingDirectory, prefix );
      buildAndRecordStatistics( context.appDirectory, archiveDir, buildSystem, isj2cl );
    }, () -> FileUtil.deleteDir( WorkspaceUtil.getArchiveDir( context.workingDirectory, context.branch + ".after" ) ) );
  }

  private static void buildAndRecordStatistics( @Nonnull final Path appDirectory,
                                                @Nonnull final Path archiveDir,
                                                @Nonnull final BuildSystem buildSystem,
                                                final boolean isj2cl )
  {
    if ( BuildSystem.BUILDR == buildSystem )
    {
      WorkspaceUtil.customizeBuildr( appDirectory );
    }
    else if ( BuildSystem.BAZEL == buildSystem )
    {
      //TODO: Customize Bazel to add local repository
    }
    else
    {
      assert BuildSystem.MAVEN == buildSystem;
      WorkspaceUtil.customizeMaven( appDirectory );
    }

    if ( !archiveDir.toFile().mkdirs() )
    {
      final String message = "Error creating archive directory: " + archiveDir;
      Gir.messenger().error( message );
    }

    if ( BuildSystem.BUILDR == buildSystem )
    {
      // Perform the build
      Ruby.buildr( "clean", "package", "EXCLUDE_GWT_DEV_MODULE=true", "GWT=react4j-todomvc" );

      archiveBuildrOutput( archiveDir );
    }
    else if ( BuildSystem.BAZEL == buildSystem )
    {
      Exec.system( "bazel", "build", ":all" );

      archiveBazelOutput( archiveDir );
    }
    else
    {
      // Assume maven
      Exec.system( "mvn", "clean", "package" );

      if ( isj2cl )
      {
        archivej2clOutput( archiveDir );
      }
      else
      {
        archiveMavenOutput( archiveDir );
      }
    }
    archiveStatistics( archiveDir );
    if ( BuildSystem.BAZEL == buildSystem )
    {
      //TODO: Patch dependencies.bzl to replace local repository url with maven central
    }
    else if ( BuildSystem.MAVEN == buildSystem )
    {
      // Reset is required to remove changes that were made to the pom to add local repository
      Git.resetBranch();
    }
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

  private static void archiveBazelOutput( @Nonnull final Path archiveDir )
  {
    final Path currentDirectory = FileUtil.getCurrentDirectory();
    //TODO: Archive the appropriate files
  }

  private static void archivej2clOutput( @Nonnull final Path archiveDir )
  {
    final Path currentDirectory = FileUtil.getCurrentDirectory();
    final Path webAppDir = currentDirectory.resolve( "target/react4j-todomvc-1.0.0-SNAPSHOT" );
    WorkspaceUtil.archiveDirectory( webAppDir, archiveDir.resolve( "sources" ) );
    WorkspaceUtil.archiveFile( webAppDir.resolve( "react4j-todomvc/react4j-todomvc.js" ),
                               archiveDir.resolve( "assets/todomvc/todomvc.nocache.js" ) );
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
