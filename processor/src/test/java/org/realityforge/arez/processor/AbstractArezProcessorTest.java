package org.realityforge.arez.processor;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourceSubjectFactory;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.tools.JavaFileObject;
import static com.google.common.truth.Truth.*;
import static org.testng.Assert.*;

@SuppressWarnings( "Duplicates" )
abstract class AbstractArezProcessorTest
{
  void assertSuccessfulCompile( @Nonnull final String classname, final boolean repositoryEnabled )
    throws Exception
  {
    final String[] elements = classname.contains( "." ) ? classname.split( "\\." ) : new String[]{ classname };
    final StringBuilder input = new StringBuilder();
    final StringBuilder arezComponent = new StringBuilder();
    final StringBuilder repository = repositoryEnabled ? new StringBuilder() : null;
    final StringBuilder arezRepository = repositoryEnabled ? new StringBuilder() : null;
    final StringBuilder repositoryExtension = repositoryEnabled ? new StringBuilder() : null;
    input.append( "input" );
    arezComponent.append( "expected" );
    if ( repositoryEnabled )
    {
      repository.append( "expected" );
      arezRepository.append( "expected" );
      repositoryExtension.append( "expected" );
    }
    for ( int i = 0; i < elements.length; i++ )
    {
      input.append( '/' );
      input.append( elements[ i ] );
      arezComponent.append( '/' );
      if ( repositoryEnabled )
      {
        repository.append( '/' );
        arezRepository.append( '/' );
        repositoryExtension.append( '/' );
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
    }
    input.append( ".java" );
    arezComponent.append( ".java" );
    if ( repositoryEnabled )
    {
      repository.append( ".java" );
      arezRepository.append( ".java" );
      repositoryExtension.append( ".java" );
      assertSuccessfulCompile( input.toString(),
                               arezComponent.toString(),
                               repository.toString(),
                               arezRepository.toString(),
                               repositoryExtension.toString() );
    }
    else
    {
      assertSuccessfulCompile( input.toString(), arezComponent.toString() );
    }
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
      final ImmutableList<JavaFileObject> fileObjects =
        Compiler.javac().withProcessors( new ArezProcessor() ).compile( inputs ).generatedSourceFiles();
      for ( final JavaFileObject fileObject : fileObjects )
      {
        final Path target = fixtureDir().resolve( "expected/" + fileObject.getName().replace( "/SOURCE_OUTPUT/", "" ) );
        if ( Files.exists( target ) )
        {
          Files.delete( target );
        }

        final File dir = target.getParent().toFile();
        if ( !dir.exists() )
        {
          assertTrue( dir.mkdirs() );
        }
        Files.copy( fileObject.openInputStream(), target );
      }
    }
    final JavaFileObject firstExpected = fixture( outputs.get( 0 ) );
    final JavaFileObject[] restExpected =
      outputs.stream().skip( 1 ).map( this::fixture ).
        collect( Collectors.toList() ).
        toArray( new JavaFileObject[ 0 ] );
    assert_().about( JavaSourcesSubjectFactory.javaSources() ).
      that( inputs ).
      processedWith( new ArezProcessor() ).
      compilesWithoutError().
      and().
      generatesSources( firstExpected, restExpected );
  }

  void assertFailedCompile( @Nonnull final String classname, @Nonnull final String errorMessageFragment )
    throws Exception
  {
    final String[] elements = classname.contains( "." ) ? classname.split( "\\." ) : new String[]{ classname };
    final StringBuilder input = new StringBuilder();
    input.append( "bad_input" );
    for ( final String element : elements )
    {
      input.append( '/' );
      input.append( element );
    }
    input.append( ".java" );
    assertFailedCompileResource( input.toString(), errorMessageFragment );
  }

  private void assertFailedCompileResource( @Nonnull final String inputResource,
                                            @Nonnull final String errorMessageFragment )
    throws Exception
  {
    final JavaFileObject source = fixture( inputResource );
    assert_().about( JavaSourceSubjectFactory.javaSource() ).
      that( source ).
      processedWith( new ArezProcessor() ).
      failsToCompile().
      withErrorContaining( errorMessageFragment );
  }

  @Nonnull
  private JavaFileObject fixture( @Nonnull final String path )
  {
    try
    {
      return JavaFileObjects.forResource( fixtureDir().resolve( path ).toUri().toURL() );
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
