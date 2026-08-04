package arez.persist.processor;

import arez.processor.ArezProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ArezPersistProcessorJavacTest
  extends ArezPersistProcessorTestSupport
{
  @Test
  public void formatGeneratedSourceFailsClearlyWithoutJdkExports()
    throws Exception
  {
    final Path source =
      fixtureDir().resolve( "input" ).resolve( toFilename( "com.example.persist.BasicPersistModel" ) );
    assertTrue( Files.exists( source ), "Expected smoke source to exist at " + source );

    final Path classOutput = Files.createTempDirectory( "arez-persist-format-no-exports-classes" );
    final Path sourceOutput = Files.createTempDirectory( "arez-persist-format-no-exports-sources" );
    try
    {
      final Path javac = Path.of( System.getProperty( "java.home" ), "bin", "javac" );
      assertTrue( Files.exists( javac ), "Expected javac to exist at " + javac );

      final List<String> command = new ArrayList<>();
      command.add( javac.toString() );
      command.add( "-cp" );
      command.add( System.getProperty( "java.class.path" ) );
      command.add( "-processorpath" );
      command.add( System.getProperty( "java.class.path" ) );
      command.add( "-processor" );
      command.add( ArezPersistProcessor.class.getName() + "," + ArezProcessor.class.getName() );
      command.add( "-d" );
      command.add( classOutput.toString() );
      command.add( "-s" );
      command.add( sourceOutput.toString() );
      command.addAll( getOptions() );
      command.add( "-Aarez.persist.format_generated_source=true" );
      command.add( source.toString() );

      final Process process =
        new ProcessBuilder( command ).
          redirectErrorStream( true ).
          start();
      final String output = new String( process.getInputStream().readAllBytes(), StandardCharsets.UTF_8 );
      final int exitCode = process.waitFor();

      assertNotEquals( exitCode, 0, "Expected javac to fail without formatter JDK exports. Output:\n" + output );
      assertTrue( output.contains( "arez.persist.format_generated_source" ),
                  "Expected diagnostic to mention arez.persist.format_generated_source. Output:\n" + output );
      for ( final String export : formatterJdkExports() )
      {
        assertTrue( output.contains( export ),
                    "Expected diagnostic to mention required export " + export + ". Output:\n" + output );
      }
    }
    finally
    {
      deleteDir( sourceOutput );
      deleteDir( classOutput );
    }
  }

  @SuppressWarnings( "ResultOfMethodCallIgnored" )
  private void deleteDir( @Nonnull final Path directory )
  {
    try ( var paths = Files.walk( directory ) )
    {
      paths.sorted( Comparator.reverseOrder() ).map( Path::toFile ).forEach( File::delete );
    }
    catch ( final IOException e )
    {
      throw new IllegalStateException( "Failure to delete directory: " + directory, e );
    }
  }
}
