package arez.integration.util;

import arez.Arez;
import arez.ArezContext;
import arez.spy.SerializableEvent;
import arez.spy.SpyEventHandler;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

/**
 * A recorder used to record collect spy events and compare against a fixture file.
 */
public final class SpyEventRecorder
  implements SpyEventHandler
{
  @Nonnull
  private final JsonArrayBuilder _events = Json.createArrayBuilder();
  private final boolean _keepValue;

  @Nonnull
  public static SpyEventRecorder beginRecording()
  {
    return beginRecording( Arez.context() );
  }

  @Nonnull
  public static SpyEventRecorder beginRecording( @Nonnull final ArezContext context )
  {
    final SpyEventRecorder recorder = new SpyEventRecorder();
    context.getSpy().addSpyEventHandler( recorder );
    return recorder;
  }

  public SpyEventRecorder()
  {
    this( true );
  }

  public SpyEventRecorder( final boolean keepValue )
  {
    _keepValue = keepValue;
  }

  @Override
  public void onSpyEvent( @Nonnull final Object event )
  {
    if ( event instanceof SerializableEvent )
    {
      log( (SerializableEvent) event );
    }
  }

  public void mark( @Nonnull final String key, @Nonnull final Object value )
  {
    final JsonObject mark =
      Json.createObjectBuilder().add( "type", "usermark" ).add( key, String.valueOf( value ) ).build();
    _events.add( mark );
  }

  public void assertMatchesFixture( @Nonnull final Path file, final boolean updateFixture )
    throws IOException, JSONException
  {
    final String json = eventsAsString();
    if ( updateFixture )
    {
      final File dir = file.getParent().toFile();
      if ( !dir.exists() )
      {
        if ( !dir.mkdirs() )
        {
          throw new AssertionError( "Unable to create fixtures parent directory: " + dir );
        }
      }
      Files.write( file, ( json + "\n" ).getBytes() );
    }
    final String expected = String.join( "\n", Files.readAllLines( file ) );
    JSONAssert.assertEquals( expected, json, new DefaultComparator( JSONCompareMode.STRICT ) );
  }

  @Nonnull
  private String eventsAsString()
  {
    final Map<String, Object> properties = new HashMap<>( 1 );
    properties.put( JsonGenerator.PRETTY_PRINTING, true );
    final StringWriter writer = new StringWriter();
    Json.createWriterFactory( properties ).createWriter( writer ).write( _events.build() );
    writer.flush();
    return writer.toString();
  }

  private void log( @Nonnull final SerializableEvent event )
  {
    final HashMap<String, Object> map = new HashMap<>();
    event.toMap( map );
    map.remove( "duration" );
    if ( !_keepValue )
    {
      map.remove( "value" );
    }
    final HashMap<String, Object> output = new HashMap<>();
    for ( final Map.Entry<String, Object> entry : map.entrySet() )
    {
      final String key = entry.getKey();
      final Object value = entry.getValue();
      if ( key.equals( "value" ) && value instanceof Collection )
      {
        // Useful for debugging repositories
        output.put( key, ( (Collection<?>) value ).size() + " items" );
      }
      else if ( null != value && value.getClass().isArray() )
      {
        final ArrayList<Object> v = new ArrayList<>();
        for ( int i = 0, end = Array.getLength( value ); i < end; i++ )
        {
          v.add( Array.get( value, i ) );
        }
        output.put( key, v );
      }
      else
      {
        output.put( key, value );
      }
    }
    _events.add( Json.createObjectBuilder( output ) );
  }
}
