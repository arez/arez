package arez.integration;

import arez.SpyEventHandler;
import arez.spy.SerializableEvent;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonGenerator;

/**
 * A recorder used to record results of test run.
 */
public final class SpyEventRecorder
  implements SpyEventHandler
{
  private final JsonArrayBuilder _events = Json.createArrayBuilder();
  private final boolean _keepValue;

  public SpyEventRecorder()
  {
    this( true );
  }

  public SpyEventRecorder( final boolean keepValue )
  {
    _keepValue = keepValue;
  }

  @Override
  public final void onSpyEvent( @Nonnull final Object event )
  {
    if ( event instanceof SerializableEvent )
    {
      log( (SerializableEvent) event );
    }
  }

  @Nonnull
  public JsonArrayBuilder getEvents()
  {
    return _events;
  }

  @Nonnull
  public String eventsAsString()
  {
    final Map<String, Object> properties = new HashMap<>( 1 );
    properties.put( JsonGenerator.PRETTY_PRINTING, true );
    final StringWriter writer = new StringWriter();
    Json.createWriterFactory( properties ).createWriter( writer ).write( getEvents().build() );
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
        output.put( key, ( (Collection) value ).size() + " items" );
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
