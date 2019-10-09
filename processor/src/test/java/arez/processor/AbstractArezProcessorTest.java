package arez.processor;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import static com.google.common.truth.Truth.*;
import static org.testng.Assert.*;

abstract class AbstractArezProcessorTest
{
  void assertSuccessfulCompile( @Nonnull final String classname,
                                final boolean daggerModuleExpected,
                                final boolean daggerComponentExtensionExpected,
                                final boolean repositoryEnabled,
                                final boolean repositoryDaggerEnabled )
    throws Exception
  {
    final String[] elements = classname.contains( "." ) ? classname.split( "\\." ) : new String[]{ classname };
    final StringBuilder input = new StringBuilder();
    final StringBuilder arezComponent = new StringBuilder();
    final StringBuilder repository = repositoryEnabled ? new StringBuilder() : null;
    final StringBuilder arezRepository = repositoryEnabled ? new StringBuilder() : null;
    final StringBuilder componentDaggerModule = daggerModuleExpected ? new StringBuilder() : null;
    final StringBuilder componentExtension = daggerComponentExtensionExpected ? new StringBuilder() : null;
    final StringBuilder repositoryExtension = repositoryEnabled ? new StringBuilder() : null;
    final StringBuilder repositoryDaggerModule = repositoryDaggerEnabled ? new StringBuilder() : null;
    input.append( "input" );
    arezComponent.append( "expected" );
    if ( daggerModuleExpected )
    {
      componentDaggerModule.append( "expected" );
    }
    if ( daggerComponentExtensionExpected )
    {
      componentExtension.append( "expected" );
    }
    if ( repositoryEnabled )
    {
      repository.append( "expected" );
      arezRepository.append( "expected" );
      repositoryExtension.append( "expected" );
    }
    if ( repositoryDaggerEnabled )
    {
      repositoryDaggerModule.append( "expected" );
    }
    for ( int i = 0; i < elements.length; i++ )
    {
      input.append( '/' );
      input.append( elements[ i ] );
      arezComponent.append( '/' );
      if ( daggerModuleExpected )
      {
        componentDaggerModule.append( '/' );
      }
      if ( daggerComponentExtensionExpected )
      {
        componentExtension.append( '/' );
      }
      if ( repositoryEnabled )
      {
        repository.append( '/' );
        arezRepository.append( '/' );
        repositoryExtension.append( '/' );
      }
      if ( repositoryDaggerEnabled )
      {
        repositoryDaggerModule.append( '/' );
      }
      final boolean isLastElement = i == elements.length - 1;
      if ( isLastElement )
      {
        arezComponent.append( "Arez_" );
        if ( repositoryEnabled )
        {
          arezRepository.append( "Arez_" );
        }
      }
      arezComponent.append( elements[ i ] );
      if ( daggerModuleExpected )
      {
        componentDaggerModule.append( elements[ i ] );
        if ( isLastElement )
        {
          componentDaggerModule.append( "DaggerModule" );
        }
      }
      if ( daggerComponentExtensionExpected )
      {
        componentExtension.append( elements[ i ] );
        if ( isLastElement )
        {
          componentExtension.append( "DaggerComponentExtension" );
        }
      }

      if ( repositoryEnabled )
      {
        repository.append( elements[ i ] );
        arezRepository.append( elements[ i ] );
        repositoryExtension.append( elements[ i ] );
        if ( isLastElement )
        {
          repository.append( "Repository" );
          arezRepository.append( "Repository" );
          repositoryExtension.append( "Repository" );
        }
      }
      if ( repositoryDaggerEnabled )
      {
        repositoryDaggerModule.append( elements[ i ] );
        if ( isLastElement )
        {
          repositoryDaggerModule.append( "RepositoryDaggerModule" );
        }
      }
    }
    input.append( ".java" );
    arezComponent.append( ".java" );
    final ArrayList<String> expectedOutputs = new ArrayList<>();
    expectedOutputs.add( arezComponent.toString() );
    if ( daggerModuleExpected )
    {
      componentDaggerModule.append( ".java" );
      expectedOutputs.add( componentDaggerModule.toString() );
    }
    if ( daggerComponentExtensionExpected )
    {
      componentExtension.append( ".java" );
      expectedOutputs.add( componentExtension.toString() );
    }
    if ( repositoryEnabled )
    {
      repository.append( ".java" );
      arezRepository.append( ".java" );
      repositoryExtension.append( ".java" );
      expectedOutputs.add( repository.toString() );
      expectedOutputs.add( arezRepository.toString() );
      expectedOutputs.add( repositoryExtension.toString() );
    }
    if ( repositoryDaggerEnabled )
    {
      repositoryDaggerModule.append( ".java" );
      expectedOutputs.add( repositoryDaggerModule.toString() );
    }
    assertSuccessfulCompile( input.toString(), expectedOutputs.toArray( new String[ 0 ] ) );
  }

  void assertSuccessfulCompile( @Nonnull final String inputResource, @Nonnull final String... expectedOutputResources )
    throws Exception
  {
    final JavaFileObject source = fixture( inputResource );
    assertSuccessfulCompile( Collections.singletonList( source ), Arrays.asList( expectedOutputResources ) );
  }

  void assertSuccessfulCompile( @Nonnull final List<JavaFileObject> inputs,
                                @Nonnull final List<String> outputs )
    throws Exception
  {
    if ( outputFiles() )
    {
      final Compilation compilation =
        Compiler.javac().withProcessors( new ArezProcessor() ).compile( inputs );

      final Compilation.Status status = compilation.status();
      if ( Compilation.Status.SUCCESS != status )
      {
        /*
         * Ugly hackery that marks the compile as successful so we can emit output onto filesystem. This could
         * result in java code that is not compilable emitted to filesystem. This re-running determining problems
         * a little easier even if it does make re-running tests from IDE a little harder
         */
        final Field field = compilation.getClass().getDeclaredField( "status" );
        field.setAccessible( true );
        field.set( compilation, Compilation.Status.SUCCESS );
      }

      final ImmutableList<JavaFileObject> fileObjects = compilation.generatedSourceFiles();
      for ( final JavaFileObject fileObject : fileObjects )
      {
        final Path target = fixtureDir().resolve( "expected/" + fileObject.getName().replace( "/SOURCE_OUTPUT/", "" ) );

        final File dir = target.getParent().toFile();
        if ( !dir.exists() )
        {
          assertTrue( dir.mkdirs() );
        }
        if ( Files.exists( target ) )
        {
          final byte[] existing = Files.readAllBytes( target );
          final InputStream generated = fileObject.openInputStream();
          final byte[] data = new byte[ generated.available() ];
          assertEquals( generated.read( data ), data.length );
          if ( Arrays.equals( existing, data ) )
          {
            /*
             * If the data on the filesystem is identical to data generated then do not write
             * to filesystem. The writing can be slow and it can also trigger the IDE or other
             * tools to recompile code which is problematic.
             */
            continue;
          }
          Files.delete( target );
        }
        Files.copy( fileObject.openInputStream(), target );
      }

      if ( Compilation.Status.SUCCESS != status )
      {
        // Restore old status
        final Field field = compilation.getClass().getDeclaredField( "status" );
        field.setAccessible( true );
        field.set( compilation, status );

        // This next line will generate an error
        try
        {
          //noinspection ResultOfMethodCallIgnored
          compilation.generatedSourceFiles();
        }
        catch ( final Exception ignored )
        {
        }
      }
    }
    final JavaFileObject firstExpected = fixture( outputs.get( 0 ) );
    final JavaFileObject[] restExpected =
      outputs.stream().skip( 1 ).map( this::fixture ).toArray( JavaFileObject[]::new );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( inputs ).
      withCompilerOptions( "-Xlint:all,-processing", "-implicit:none", "-Aarez.defer.errors=false" ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      and().
      generatesSources( firstExpected, restExpected );
  }

  void assertFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
  {
    assertFailedCompileResource( toFilename( "bad_input", classname ), errorMessageFragment );
  }

  @SuppressWarnings( "SameParameterValue" )
  @Nonnull
  final String toFilename( @Nonnull final String dir, @Nonnull final String classname )
  {
    final String[] elements = classname.contains( "." ) ? classname.split( "\\." ) : new String[]{ classname };
    final StringBuilder input = new StringBuilder();
    input.append( dir );
    for ( final String element : elements )
    {
      input.append( '/' );
      input.append( element );
    }
    input.append( ".java" );
    return input.toString();
  }

  private void assertFailedCompileResource( @Nonnull final String inputResource,
                                            @Nonnull final String errorMessageFragment )
  {
    assertFailedCompileResource( Collections.singletonList( fixture( inputResource ) ), errorMessageFragment );
  }

  void assertFailedCompileResource( @Nonnull final List<JavaFileObject> inputs,
                                    @Nonnull final String errorMessageFragment )
  {
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( inputs ).
      processedWith( new ArezProcessor() ).
      failsToCompile().
      withWarningContaining( errorMessageFragment );
  }

  @Nonnull
  final JavaFileObject fixture( @Nonnull final String path )
  {
    final Path outputFile = fixtureDir().resolve( path );
    if ( !Files.exists( outputFile ) )
    {
      fail( "Output file fixture " + outputFile + " does not exist. Thus can not compare against it." );
    }
    try
    {
      return JavaFileObjects.forResource( outputFile.toUri().toURL() );
    }
    catch ( final MalformedURLException e )
    {
      throw new IllegalStateException( e );
    }
  }

  @Nonnull
  private Path fixtureDir()
  {
    final String fixtureDir = System.getProperty( "arez.fixture_dir" );
    assertNotNull( fixtureDir, "Expected System.getProperty( \"arez.fixture_dir\" ) to return fixture directory" );
    return new File( fixtureDir ).toPath();
  }

  private boolean outputFiles()
  {
    return System.getProperty( "arez.output_fixture_data", "false" ).equals( "true" );
  }
}
