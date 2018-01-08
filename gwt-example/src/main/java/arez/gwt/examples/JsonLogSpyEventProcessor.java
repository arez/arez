package arez.gwt.examples;

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
import elemental2.dom.DomGlobal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import jsinterop.base.JsPropertyMap;
import org.realityforge.anodoc.Unsupported;

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
