package arez.testutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import org.realityforge.braincheck.BrainCheckTestUtil;

/**
 * This class is used by test infrastructure to collect and match all invocations invariant checks
 * in the {@link org.realityforge.braincheck.Guards} class. This class will be invoked any time
 * {@link org.realityforge.braincheck.Guards#invariant(Supplier, Supplier)} is invoked or
 * {@link org.realityforge.braincheck.Guards#apiInvariant(Supplier, Supplier)} is invoked and will
 * receive the message. If the message matches the pattern <code>"[Key]-####: [Message]"</code>
 * and this class has been configured to match <code>Key</code> message then this message will be
 * recorded or matched against predefined message template.
 *
 * <p>The message templates are recorded in a json file. The json contains an array of entries of the
 * form belows. The <code>messagePattern</code> property can also include the value <code>%s</code> which
 * is replaced with <code>.*</code> which can be replaced by any string during matching.</p>
 *
 * <pre>
 *     {
 *     "code": 1,
 *     "type": "API_INVARIANT",
 *     "messagePattern": "Invoked Arez.createZone() but zones are not enabled.",
 *     "callers": [
 *       {
 *         "class": "arez.Arez",
 *         "method": "createZone",
 *         "file": "Arez.java",
 *         "lineNumber": 185
 *       }
 *     ]
 *   }
 * </pre>
 *
 * <p>The class makes a few assumptions. It assumes that every invariant message with the specified
 * <code>key</code> is invoked while the collector is active and thus if a message does not occur that
 * it no longer exists. It also assumes that only invariant messages that match the above pattern are
 * of interest.</p>
 *
 * <p>To enable this class it is typical to hook it into appropriate places according to your test
 * framework of choice. In TestNG the base test class may look like:</p>
 *
 * <pre>
 * public abstract class AbstractTest
 * {
 *   private static final GuardMessageCollector c_messages = createCollector();
 *
 *   {@code @}BeforeSuite
 *   protected void beforeSuite() { c_messages.onTestSuiteStart(); }
 *
 *   {@code @}BeforeMethod
 *   protected void beforeTest() { c_messages.onTestStart(); }
 *
 *   {@code @}AfterMethod
 *   protected void afterTest() { c_messages.onTestComplete(); }
 *
 *   {@code @}AfterSuite
 *   protected void afterSuite() { c_messages.onTestSuiteComplete(); }
 *
 *   private static GuardMessageCollector createCollector() { ... }
 * }
 * </pre>
 *
 * <p>The primary purpose of the class is to enable the validation and collection of invariant checks
 * for the purpose of documentation. The output file includes the invariant pattern as well as the
 * location in the source file where the invariant checks are generated. This makes it easy to
 * generate errors with cross references as required.</p>
 */
public final class GuardMessageCollector
{
  @Nonnull
  private final Map<Integer, Message> _messages = new HashMap<>();
  @Nonnull
  private final String _key;
  @Nonnull
  private final File _file;
  private final boolean _saveIfChanged;
  private long _loadTime;

  /**
   * Create the collector.
   *
   * @param key           the key/prefix used when selecting messages to match.
   * @param file          the file expected to contain message templates. This file need not exist if <code>saveIfChanged</code> is <code>true</code>.
   * @param saveIfChanged flag set to true if changed message templates should be saved to <code>file</code>.
   */
  public GuardMessageCollector( @Nonnull final String key,
                                @Nonnull final File file,
                                final boolean saveIfChanged )
  {
    _key = Objects.requireNonNull( key );
    _file = Objects.requireNonNull( file );
    _saveIfChanged = saveIfChanged;
  }

  /**
   * Hook method that should be invoked before any test starts.
   * This method will load the message templates if the file exists otherwise it will just reset internal state.
   */
  public void onTestSuiteStart()
  {
    loadIfRequired();
  }

  /**
   * Hook method that should be invoked before a test starts.
   */
  public void onTestStart()
  {
    BrainCheckTestUtil.setOnGuardListener( this::onGuardInvoked );
  }

  /**
   * Hook method that should be invoked after a test completes.
   */
  public void onTestComplete()
  {
    BrainCheckTestUtil.setOnGuardListener( null );
  }

  /**
   * Hook method that should be invoked after all tests have completed.
   * If the expected invariant invocations no longer match the actual invariant invocations
   * then this method will either save the new message template or generate an exception depending
   * on the value of the <code>saveIfChanged</code> used to create this class.
   */
  public void onTestSuiteComplete()
  {
    if ( needsSave() )
    {
      if ( _saveIfChanged )
      {
        saveIfRequired();
      }
      else
      {
        final List<Message> unsavedMessages =
          _messages.values().stream().filter( Message::needsSave ).collect( Collectors.toList() );
        throw new IllegalStateException( "Diagnostic messages template is out of date. " + unsavedMessages.size() +
                                         " messages need to be updated including messages:\n" +
                                         unsavedMessages.stream()
                                           .map( Message::toString )
                                           .collect( Collectors.joining( "\n" ) ) );
      }
    }
  }

  private void onGuardInvoked( @Nonnull final BrainCheckTestUtil.GuardType type,
                               @Nonnull final String message,
                               @Nonnull final StackTraceElement[] stackTrace )
  {
    final Matcher matcher = Pattern.compile( "^" + _key + "-(\\d\\d\\d\\d): (.*)$" ).matcher( message );
    if ( matcher.matches() )
    {
      final int code = Integer.parseInt( matcher.group( 1 ) );
      final String msg = matcher.group( 2 );

      matchOrRecordDiagnosticMessage( code, type, msg ).recordCaller( stackTrace[ 0 ] );
    }
  }

  private void loadIfRequired()
  {
    if ( _file.exists() )
    {
      final long lastModified = _file.lastModified();
      if ( _loadTime != lastModified )
      {
        loadMessages();
        _loadTime = lastModified;
      }
    }
    else
    {
      _messages.clear();
      _loadTime = 0;
    }
  }

  private void loadMessages()
  {
    _messages.clear();
    try ( final FileInputStream inputStream = new FileInputStream( _file ) )
    {
      final JsonReader reader = Json.createReader( inputStream );
      final JsonArray top = reader.readArray();
      final int size = top.size();
      for ( int i = 0; i < size; i++ )
      {
        final JsonObject entry = top.getJsonObject( i );
        final int code = entry.getInt( "code" );
        final BrainCheckTestUtil.GuardType type = BrainCheckTestUtil.GuardType.valueOf( entry.getString( "type" ) );
        final String messagePattern = entry.getString( "messagePattern" );
        if ( _messages.containsKey( code ) )
        {
          throw new IllegalStateException( "Failed to load diagnostic messages file " + _file + " as it is " +
                                           "incorrectly formatted with duplicate entries for code " + code );
        }
        final HashSet<StackTraceElement> callers = new HashSet<>();
        final JsonArray callersData = entry.getJsonArray( "callers" );
        final int callerCount = callersData.size();
        for ( int j = 0; j < callerCount; j++ )
        {
          final JsonObject callerData = callersData.getJsonObject( j );
          final String className = callerData.getString( "class" );
          final String methodName = callerData.getString( "method" );
          final String fileName = callerData.getString( "file" );
          final int lineNumber = callerData.getInt( "lineNumber" );
          callers.add( new StackTraceElement( className, methodName, fileName, lineNumber ) );
        }
        _messages.put( code, new Message( _key, code, type, messagePattern, false, callers ) );
      }
    }
    catch ( final IOException ioe )
    {
      throw new IllegalStateException( "Failed to read diagnostic messages file " + _file + ".", ioe );
    }
  }

  private void saveIfRequired()
  {
    if ( needsSave() )
    {
      final Map<String, Object> properties = new HashMap<>();
      properties.put( JsonGenerator.PRETTY_PRINTING, true );

      final JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory( properties );
      try
      {
        try ( final FileOutputStream output = new FileOutputStream( _file ) )
        {
          final JsonGenerator g = generatorFactory.createGenerator( output );
          g.writeStartArray();
          _messages.values()
            .stream()
            .filter( m -> !m.getCallers().isEmpty() )
            .sorted( Comparator.comparingInt( Message::getCode ) )
            .forEachOrdered( m -> {
              g.writeStartObject();
              g.write( "code", m.getCode() );
              g.write( "type", m.getType().name() );
              g.write( "messagePattern", m.getMessagePattern() );

              g.writeStartArray( "callers" );
              final StackTraceElement[] callers = m.getCallers().stream().sorted().toArray( StackTraceElement[]::new );
              for ( final StackTraceElement caller : callers )
              {
                g.writeStartObject();
                g.write( "class", caller.getClassName() );
                g.write( "method", caller.getMethodName() );
                g.write( "file", caller.getFileName() );
                g.write( "lineNumber", caller.getLineNumber() );
                g.writeEnd();
              }

              g.writeEnd();
              g.writeEnd();
            } );
          g.writeEnd();
          g.close();
        }
        formatJson( _file );
      }
      catch ( final IOException ioe )
      {
        throw new IllegalStateException( "Failed to write diagnostic messages file " + _file + ".", ioe );
      }
    }
  }

  private boolean needsSave()
  {
    return _messages.values().stream().anyMatch( Message::needsSave );
  }

  /**
   * Format the json file.
   * This is horribly inefficient but it is not called very often so ... meh.
   */
  private void formatJson( @Nonnull final File file )
    throws IOException
  {
    final byte[] data = Files.readAllBytes( file.toPath() );
    final Charset charset = Charset.forName( "UTF-8" );
    final String jsonData = new String( data, charset );

    final String output =
      jsonData
        .replaceAll( "(?m)^ {4}\\{", "  {" )
        .replaceAll( "(?m)^ {4}}", "  }" )
        .replaceAll( "(?m)^ {8}\"", "    \"" )
        .replaceAll( "(?m)^ {8}]", "    ]" )
        .replaceAll( "(?m)^ {12}\\{", "      {" )
        .replaceAll( "(?m)^ {12}}", "      }" )
        .replaceAll( "(?m)^ {16}\"", "        \"" )
        .replaceAll( "(?m)^\n\\[\n", "[\n" ) +
      "\n";
    Files.write( file.toPath(), output.getBytes( charset ) );
  }

  @Nonnull
  private Message recordDiagnosticMessage( final int code,
                                           @Nonnull final BrainCheckTestUtil.GuardType type,
                                           @Nonnull final String messagePattern )
  {
    final Message message =
      new Message( _key, code, type, messagePattern, true, new HashSet<>() );
    _messages.put( code, message );
    return message;
  }

  @Nonnull
  private Message matchOrRecordDiagnosticMessage( final int code,
                                                  @Nonnull final BrainCheckTestUtil.GuardType type,
                                                  @Nonnull final String message )
  {
    final Message m = _messages.get( code );
    if ( null == m )
    {
      return recordDiagnosticMessage( code, type, message );
    }
    else
    {
      final StringBuilder sb = new StringBuilder();

      String messagePattern = m.getMessagePattern();
      int lastOffset = 0;
      int offset;
      while ( -1 != ( offset = messagePattern.indexOf( "%s", lastOffset ) ) )
      {
        final String segment = messagePattern.substring( lastOffset, offset );
        sb.append( Pattern.quote( segment ) );
        sb.append( ".*" );
        lastOffset = offset + 2;
      }
      sb.append( Pattern.quote( messagePattern.substring( lastOffset ) ) );

      final Pattern pattern = Pattern.compile( sb.toString() );
      if ( !pattern.matcher( message ).matches() )
      {
        throw new IllegalStateException( "Failed to match " + type + " diagnostic message with " +
                                         "prefix " + _key + " and code " + code + ".\n" +
                                         "Expected pattern:\n" + messagePattern + "\n\n" +
                                         "Actual Message:\n" + message );
      }
      else if ( !Objects.equals( m.getType(), type ) )
      {
        final String message1 =
          "Failed to match diagnostic message type with prefix " + _key + " and code " + code + ".";
        throw new AssertionError( message1 );
      }
      return m;
    }
  }

  /**
   * Class representing a message of a single code.
   */
  private static final class Message
  {
    @Nonnull
    private final String _prefix;
    private final int _code;
    @Nonnull
    private final BrainCheckTestUtil.GuardType _type;
    @Nonnull
    private final String _messagePattern;
    private final boolean _needsSave;
    private final Set<StackTraceElement> _originalCallers = new HashSet<>();
    private final Set<StackTraceElement> _callers = new HashSet<>();

    Message( @Nonnull final String prefix,
             final int code,
             @Nonnull final BrainCheckTestUtil.GuardType type,
             @Nonnull final String messagePattern,
             final boolean needsSave,
             @Nonnull final Set<StackTraceElement> callers )
    {
      _prefix = Objects.requireNonNull( prefix );
      _code = code;
      _type = Objects.requireNonNull( type );
      _messagePattern = Objects.requireNonNull( messagePattern );
      _needsSave = needsSave;
      _originalCallers.addAll( Objects.requireNonNull( callers ) );
    }

    int getCode()
    {
      return _code;
    }

    @Nonnull
    BrainCheckTestUtil.GuardType getType()
    {
      return _type;
    }

    @Nonnull
    String getMessagePattern()
    {
      return _messagePattern;
    }

    boolean needsSave()
    {
      return _needsSave || !Objects.equals( _originalCallers, _callers ) || _originalCallers.isEmpty();
    }

    void recordCaller( @Nonnull final StackTraceElement caller )
    {
      _callers.add( caller );
    }

    @Nonnull
    Set<StackTraceElement> getCallers()
    {
      return Collections.unmodifiableSet( _callers );
    }

    @Override
    public String toString()
    {
      return _prefix + "-" + getCode() + ": " + getMessagePattern();
    }
  }
}
