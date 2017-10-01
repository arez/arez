package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
import jsinterop.base.JsPropertyMap;
import jsinterop.base.JsPropertyMapOfAny;
import org.realityforge.arez.extras.spy.SpyUtil;
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

final class GwtExamplesSpyUtil
{
  private static final Map<Class, EventEmitter> _emitterMap = new HashMap<>();
  private static int _indentLevel;

  private GwtExamplesSpyUtil()
  {
  }

  static
  {
    on( ObserverCreatedEvent.class,
        e -> "Observer Created " + e.getObserver().getName() );
    on( ObserverCreatedEvent.class,
        e -> "Observer Created " + e.getObserver().getName() );
    on( ObserverDisposedEvent.class,
        e -> "Observer Disposed " + e.getObserver().getName() );
    on( ObserverErrorEvent.class,
        e -> "Observer Error " + e.getObserver().getName() + " " + e.getError() + " " + e.getThrowable() );
    on( ObservableCreatedEvent.class,
        e -> "Observable Created " + e.getObservable().getName() );
    on( ObservableDisposedEvent.class,
        e -> "Observable Disposed " + e.getObservable().getName() );
    on( ObservableChangedEvent.class,
        e -> "Observable Changed " + e.getObservable().getName() );
    on( ComputedValueActivatedEvent.class,
        e -> "Computed Value Activate " + e.getComputedValue().getName() );
    on( ComputedValueDeactivatedEvent.class,
        e -> "Computed Value Deactivate " + e.getComputedValue().getName() );
    on( ComputedValueCreatedEvent.class,
        e -> "Computed Value Created " + e.getComputedValue().getName() );
    on( ComputedValueDisposedEvent.class,
        e -> "Computed Value Disposed " + e.getComputedValue().getName() );
    on( ReactionStartedEvent.class,
        e -> "Reaction Started " + e.getObserver().getName() );
    on( ReactionScheduledEvent.class,
        e -> "Reaction Scheduled " + e.getObserver().getName() );
    on( ReactionCompletedEvent.class,
        e -> "Reaction Completed " + e.getObserver().getName() + " [" + e.getDuration() + "]" );
    on( TransactionStartedEvent.class,
        e -> "Transaction Started " +
             e.getName() +
             " Mutation=" +
             e.isMutation() +
             " Tracker=" +
             ( e.getTracker() == null ? null : e.getTracker().getName() ) );
    on( TransactionCompletedEvent.class,
        e -> "Transaction Completed " +
             e.getName() +
             " Mutation=" +
             e.isMutation() +
             " Tracker=" +
             ( e.getTracker() == null ? null : e.getTracker().getName() ) + " [" + e.getDuration() + "]" );
    on( ComputeStartedEvent.class,
        e -> "Compute Started " + e.getComputedValue().getName() );
    on( ComputeCompletedEvent.class,
        e -> "Compute Completed " + e.getComputedValue().getName() + " [" + e.getDuration() + "]" );
    on( ActionStartedEvent.class,
        e -> ( e.isTracked() ? "Tracked " : "" ) + "Action Started " +
             e.getName() +
             "(" +
             Arrays.toString( e.getParameters() ) +
             ")" );
    on( ActionCompletedEvent.class,
        e -> ( e.isTracked() ? "Tracked " : "" ) + "Action Completed " +
             e.getName() +
             "(" +
             Arrays.toString( e.getParameters() ) +
             ")" + ( e.isExpectsResult() && null == e.getThrowable() ? " = " + e.getResult() : "" ) +
             ( null != e.getThrowable() ? "threw " + e.getThrowable() : "" ) +
             " Duration [" +
             e.getDuration() +
             "]" );
  }

  private static <T extends SerializableEvent> void on( @Nonnull final Class<T> type,
                                                        @Nonnull final Function<T, String> processor )
  {
    _emitterMap.put( type, new EventEmitter<>( processor ) );
  }

  @SuppressWarnings( "unchecked" )
  static void emitEvent( @Nonnull final Object event )
  {
    final EventEmitter emitter = _emitterMap.get( event.getClass() );
    if ( null != emitter )
    {
      final SpyUtil.NestingDelta delta = SpyUtil.getNestingDelta( event.getClass() );
      _indentLevel += SpyUtil.NestingDelta.DECREASE == delta ? -1 : 0;

      final StringBuilder sb = new StringBuilder();
      for ( int i = 0; i < _indentLevel; i++ )
      {
        sb.append( "  " );
      }
      sb.append( emitter.getProcessor().apply( event ) );

      DomGlobal.console.log( sb.toString() );
      final Map<String, Object> bag = new HashMap<>();
      final SerializableEvent sEvent = (SerializableEvent) event;
      sEvent.toMap( bag );
      final JsPropertyMapOfAny json = JsPropertyMap.of();
      for ( final Map.Entry<String, Object> entry : bag.entrySet() )
      {
        final Object value = entry.getValue();
        if ( value instanceof Byte )
        {
          json.set( entry.getKey(), ( (Byte) value ).intValue() );
        }
        else if ( value instanceof Short )
        {
          json.set( entry.getKey(), ( (Short) value ).intValue() );
        }
        else if ( value instanceof Integer )
        {
          json.set( entry.getKey(), ( (Integer) value ).intValue() );
        }
        else if ( value instanceof Long )
        {
          json.set( entry.getKey(), ( (Long) value ).longValue() );
        }
        else if ( value instanceof Float )
        {
          json.set( entry.getKey(), ( (Float) value ).floatValue() );
        }
        else if ( value instanceof Double )
        {
          json.set( entry.getKey(), ( (Double) value ).floatValue() );
        }
        else
        {
          json.set( entry.getKey(), value );
        }
      }
      DomGlobal.console.log( json );
      _indentLevel += SpyUtil.NestingDelta.INCREASE == delta ? 1 : 0;
    }
    else
    {
      DomGlobal.console.log( event );
    }
  }

  private static class EventEmitter<T extends SerializableEvent>
  {
    private final Function<T, String> _processor;

    EventEmitter( @Nonnull final Function<T, String> processor )
    {
      _processor = Objects.requireNonNull( processor );
    }

    @Nonnull
    Function<T, String> getProcessor()
    {
      return _processor;
    }
  }
}
