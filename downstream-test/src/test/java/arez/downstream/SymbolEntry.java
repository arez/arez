package arez.downstream;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import static org.testng.Assert.*;

/**
 * Represents a symbol in the GWT output.
 */
final class SymbolEntry
{
  @Nonnull
  private final String _jsName;
  @Nonnull
  private final String _jsniIdent;
  @Nonnull
  private final String _className;
  @Nonnull
  private final String _memberName;
  @Nonnull
  private final String _sourceUri;
  @Nonnull
  private final String _sourceLine;
  @Nonnull
  private final String _fragmentNumber;

  private SymbolEntry( @Nonnull final String jsName,
                       @Nonnull final String jsniIdent,
                       @Nonnull final String className,
                       @Nonnull final String memberName,
                       @Nonnull final String sourceUri,
                       @Nonnull final String sourceLine,
                       @Nonnull final String fragmentNumber )
  {
    _jsName = Objects.requireNonNull( jsName );
    _jsniIdent = Objects.requireNonNull( jsniIdent );
    _className = Objects.requireNonNull( className );
    _memberName = Objects.requireNonNull( memberName );
    _sourceUri = Objects.requireNonNull( sourceUri );
    _sourceLine = Objects.requireNonNull( sourceLine );
    _fragmentNumber = Objects.requireNonNull( fragmentNumber );
  }

  /**
   * Read the symbol map from specified file and pass each symbol to supplied action.
   *
   * @param path   the path to the symbolMap file.
   * @param action the callback for each entry.
   * @throws IOException    if there is an error reading file.
   * @throws ParseException if there is an error parsing file.
   */
  static void readSymbolMap( @Nonnull final Path path, @Nonnull final Consumer<SymbolEntry> action )
    throws IOException, ParseException
  {
    final BufferedReader br = new BufferedReader( new FileReader( path.toFile() ) );

    final int permutationCount = 6;
    for ( int i = 0; i < permutationCount; i++ )
    {
      // Read the first 6 lines of symbol map which happen to be all the permutation proeprties
      // Assume that the following line is the header we care about
      br.readLine();
    }
    // Skip the comment start of headers line
    assertEquals( br.read(), '#' );
    assertEquals( br.read(), ' ' );

    final CsvReader reader = new CsvReader( br );
    if ( !reader.readHeaders() )
    {
      fail( "Failed to find headers in symbolFile " + path + " Skipped " + permutationCount +
            " permutation lines" );
    }
    checkHeader( reader, 0, "jsName" );
    checkHeader( reader, 1, "jsniIdent" );
    checkHeader( reader, 2, "className" );
    checkHeader( reader, 3, "memberName" );
    checkHeader( reader, 4, "sourceUri" );
    checkHeader( reader, 5, "sourceLine" );
    checkHeader( reader, 6, "fragmentNumber" );
    while ( reader.readRecord() )
    {
      final SymbolEntry entry =
        new SymbolEntry( reader.get( "jsName" ),
                         reader.get( "jsniIdent" ),
                         reader.get( "className" ),
                         reader.get( "memberName" ),
                         reader.get( "sourceUri" ),
                         reader.get( "sourceLine" ),
                         reader.get( "fragmentNumber" ) );
      action.accept( entry );
    }
    reader.close();
  }

  private static void checkHeader( @Nonnull final CsvReader reader, final int number, @Nonnull final String expected )
    throws ParseException, IOException
  {
    final String header = reader.getHeader( number );
    assertEquals( header,
                  expected,
                  String.format( "%nExpected Header :- [%50s],%nResolved Header :- [%50s]", expected, header ) );
  }

  @Nonnull
  String getJsName()
  {
    return _jsName;
  }

  @Nonnull
  String getJsniIdent()
  {
    return _jsniIdent;
  }

  @Nonnull
  String getClassName()
  {
    return _className;
  }

  @Nonnull
  String getMemberName()
  {
    return _memberName;
  }

  @Nonnull
  String getSourceUri()
  {
    return _sourceUri;
  }

  @Nonnull
  String getSourceLine()
  {
    return _sourceLine;
  }

  @Nonnull
  String getFragmentNumber()
  {
    return _fragmentNumber;
  }
}
