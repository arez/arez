package arez.integration;

import arez.extras.spy.AbstractSpyEventProcessor;
import arez.spy.ActionCompletedEvent;
import arez.spy.ActionStartedEvent;
import arez.spy.ComponentCreateCompletedEvent;
import arez.spy.ComponentCreateStartedEvent;
import arez.spy.ComponentDisposeCompletedEvent;
import arez.spy.ComponentDisposeStartedEvent;
import arez.spy.ComputeCompletedEvent;
import arez.spy.ComputeStartedEvent;
import arez.spy.ComputedValueActivatedEvent;
import arez.spy.ComputedValueCreatedEvent;
import arez.spy.ComputedValueDeactivatedEvent;
import arez.spy.ComputedValueDisposedEvent;
import arez.spy.ObservableChangedEvent;
import arez.spy.ObservableCreatedEvent;
import arez.spy.ObservableDisposedEvent;
import arez.spy.ObserverCreatedEvent;
import arez.spy.ObserverDisposedEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.ReactionCompletedEvent;
import arez.spy.ReactionScheduledEvent;
import arez.spy.ReactionStartedEvent;
import arez.spy.SerializableEvent;
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
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
final class SpyEventRecorder
  extends AbstractSpyEventProcessor
{
  private final JsonArrayBuilder _events = Json.createArrayBuilder();
  private final boolean _keepValue;

  SpyEventRecorder()
  {
    this( true );
  }

  SpyEventRecorder( final boolean keepValue )
  {
    _keepValue = keepValue;
    register( ComponentCreateStartedEvent.class );
    register( ComponentCreateCompletedEvent.class );
    register( ComponentDisposeStartedEvent.class );
    register( ComponentDisposeCompletedEvent.class );
    register( ObserverCreatedEvent.class );
    register( ObserverDisposedEvent.class );
    register( ObserverErrorEvent.class );
    register( ObservableCreatedEvent.class );
    register( ObservableDisposedEvent.class );
    register( ObservableChangedEvent.class );
    register( ComputedValueActivatedEvent.class );
    register( ComputedValueDeactivatedEvent.class );
    register( ComputedValueCreatedEvent.class );
    register( ComputedValueDisposedEvent.class );
    register( ReactionStartedEvent.class );
    register( ReactionScheduledEvent.class );
    register( ReactionCompletedEvent.class );
    register( TransactionStartedEvent.class );
    register( TransactionCompletedEvent.class );
    register( ComputeStartedEvent.class );
    register( ComputeCompletedEvent.class );
    register( ActionStartedEvent.class );
    register( ActionCompletedEvent.class );
  }

  private <T extends SerializableEvent> void register( @Nonnull final Class<T> type )
  {
    super.on( type, ( d, e ) -> log( e ) );
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

  @Nonnull
  JsonArrayBuilder getEvents()
  {
    return _events;
  }

  @Nonnull
  String eventsAsString()
  {
    final Map<String, Object> properties = new HashMap<>( 1 );
    properties.put( JsonGenerator.PRETTY_PRINTING, true );
    final StringWriter writer = new StringWriter();
    Json.createWriterFactory( properties ).createWriter( writer ).write( getEvents().build() );
    writer.flush();
    return writer.toString();
  }
}
