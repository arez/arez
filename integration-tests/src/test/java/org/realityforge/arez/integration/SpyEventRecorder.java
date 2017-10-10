package org.realityforge.arez.integration;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.stream.JsonGenerator;
import org.realityforge.arez.extras.spy.AbstractSpyEventProcessor;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComputeCompletedEvent;
import org.realityforge.arez.spy.ComputeStartedEvent;
import org.realityforge.arez.spy.ComputedValueActivatedEvent;
import org.realityforge.arez.spy.ComputedValueCreatedEvent;
import org.realityforge.arez.spy.ComputedValueDeactivatedEvent;
import org.realityforge.arez.spy.ComputedValueDisposedEvent;
import org.realityforge.arez.spy.ObservableChangedEvent;
import org.realityforge.arez.spy.ObservableCreatedEvent;
import org.realityforge.arez.spy.ObservableDisposedEvent;
import org.realityforge.arez.spy.ObserverCreatedEvent;
import org.realityforge.arez.spy.ObserverDisposedEvent;
import org.realityforge.arez.spy.ObserverErrorEvent;
import org.realityforge.arez.spy.ReactionCompletedEvent;
import org.realityforge.arez.spy.ReactionScheduledEvent;
import org.realityforge.arez.spy.ReactionStartedEvent;
import org.realityforge.arez.spy.SerializableEvent;
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;

/**
 * A recorder used to record results of test run.
 */
final class SpyEventRecorder
  extends AbstractSpyEventProcessor
{
  private final JsonArrayBuilder _events = Json.createArrayBuilder();

  SpyEventRecorder()
  {
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
    _events.add( Json.createObjectBuilder( map ) );
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
