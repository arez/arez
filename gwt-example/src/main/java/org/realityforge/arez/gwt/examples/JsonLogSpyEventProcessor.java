package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import jsinterop.base.JsPropertyMap;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.extras.spy.AbstractSpyEventProcessor;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComponentCreateCompletedEvent;
import org.realityforge.arez.spy.ComponentCreateStartedEvent;
import org.realityforge.arez.spy.ComponentDisposeCompletedEvent;
import org.realityforge.arez.spy.ComponentDisposeStartedEvent;
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

@Unsupported
final class JsonLogSpyEventProcessor
  extends AbstractSpyEventProcessor
{
  JsonLogSpyEventProcessor()
  {
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

  @SuppressWarnings( "UnnecessaryUnboxing" )
  private void log( @Nonnull final SerializableEvent event )
  {
    final HashMap<String, Object> map = new HashMap<>();
    event.toMap( map );
    final JsPropertyMap<Object> data = JsPropertyMap.of();
    for ( final Map.Entry<String, Object> entry : map.entrySet() )
    {
      final String key = entry.getKey();
      final Object value = entry.getValue();
      if ( value instanceof Byte )
      {
        data.set( key, ( (Byte) value ).intValue() );
      }
      else if ( value instanceof Short )
      {
        data.set( key, ( (Short) value ).intValue() );
      }
      else if ( value instanceof Integer )
      {
        data.set( key, ( (Integer) value ).intValue() );
      }
      else if ( value instanceof Long )
      {
        data.set( key, ( (Long) value ).intValue() );
      }
      else if ( value instanceof Float )
      {
        data.set( key, ( (Float) value ).floatValue() );
      }
      else if ( value instanceof Double )
      {
        data.set( key, ( (Double) value ).floatValue() );
      }
      else
      {
        data.set( key, value );
      }
    }
    DomGlobal.console.log( data );
  }
}
