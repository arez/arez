package org.realityforge.arez.gwt.examples;

import elemental2.dom.DomGlobal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
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
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;

final class SpyUtil
{
  private static final Map<Class, EventEmitter> _emitterMap = new HashMap<>();
  private static int _indentLevel;

  private SpyUtil()
  {
  }

  static
  {
    on( ObserverCreatedEvent.class,
        IndentType.NONE,
        e -> "Observer Created " + e.getObserver().getName() );
    on( ObserverCreatedEvent.class,
        IndentType.NONE,
        e -> "Observer Created " + e.getObserver().getName() );
    on( ObserverDisposedEvent.class,
        IndentType.NONE,
        e -> "Observer Disposed " + e.getObserver().getName() );
    on( ObserverErrorEvent.class,
        IndentType.NONE,
        e -> "Observer Error " + e.getObserver().getName() + " " + e.getError() + " " + e.getThrowable() );
    on( ObservableCreatedEvent.class,
        IndentType.NONE,
        e -> "Observable Created " + e.getObservable().getName() );
    on( ObservableDisposedEvent.class,
        IndentType.NONE,
        e -> "Observable Disposed " + e.getObservable().getName() );
    on( ObservableChangedEvent.class,
        IndentType.NONE,
        e -> "Observable Changed " + e.getObservable().getName() );
    on( ComputedValueActivatedEvent.class,
        IndentType.NONE,
        e -> "Computed Value Activate " + e.getComputedValue().getName() );
    on( ComputedValueDeactivatedEvent.class,
        IndentType.NONE,
        e -> "Computed Value Deactivate " + e.getComputedValue().getName() );
    on( ComputedValueCreatedEvent.class,
        IndentType.NONE,
        e -> "Computed Value Created " + e.getComputedValue().getName() );
    on( ComputedValueDisposedEvent.class,
        IndentType.NONE,
        e -> "Computed Value Disposed " + e.getComputedValue().getName() );
    on( ReactionStartedEvent.class,
        IndentType.IN,
        e -> "Reaction Started " + e.getObserver().getName() );
    on( ReactionScheduledEvent.class,
        IndentType.NONE,
        e -> "Reaction Scheduled " + e.getObserver().getName() );
    on( ReactionCompletedEvent.class,
        IndentType.OUT,
        e -> "Reaction Completed " + e.getObserver().getName() + " [" + e.getDuration() + "]" );
    on( TransactionStartedEvent.class,
        IndentType.IN,
        e -> "Transaction Started " +
             e.getName() +
             " Mutation=" +
             e.isMutation() +
             " Tracker=" +
             ( e.getTracker() == null ? null : e.getTracker().getName() ) );
    on( TransactionCompletedEvent.class,
        IndentType.OUT,
        e -> "Transaction Completed " +
             e.getName() +
             " Mutation=" +
             e.isMutation() +
             " Tracker=" +
             ( e.getTracker() == null ? null : e.getTracker().getName() ) + " [" + e.getDuration() + "]" );
    on( ComputeStartedEvent.class,
        IndentType.IN,
        e -> "Compute Started " + e.getComputedValue().getName() );
    on( ComputeCompletedEvent.class,
        IndentType.OUT,
        e -> "Compute Completed " + e.getComputedValue().getName() + " [" + e.getDuration() + "]" );
    on( ActionStartedEvent.class,
        IndentType.IN,
        e -> ( e.isTracked() ? "Tracked " : "" ) + "Action Started " +
             e.getName() +
             "(" +
             Arrays.toString( e.getParameters() ) +
             ")" );
    on( ActionCompletedEvent.class,
        IndentType.OUT,
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

  private static <T> void on( @Nonnull final Class<T> type,
                              @Nonnull final IndentType indentType,
                              @Nonnull final Function<T, String> processor )
  {
    _emitterMap.put( type, new EventEmitter<>( indentType, processor ) );
  }

  @SuppressWarnings( "unchecked" )
  static void emitEvent( @Nonnull final Object event )
  {
    final EventEmitter emitter = _emitterMap.get( event.getClass() );
    if ( null != emitter )
    {
      _indentLevel += emitter.getIndentType() == IndentType.OUT ? -1 : 0;

      final StringBuilder sb = new StringBuilder();
      for ( int i = 0; i < _indentLevel; i++ )
      {
        sb.append( "  " );
      }
      sb.append( emitter.getProcessor().apply( event ) );

      DomGlobal.console.log( sb.toString() );
      _indentLevel += emitter.getIndentType() == IndentType.IN ? 1 : 0;
    }
    else
    {
      DomGlobal.console.log( event );
    }
  }

  enum IndentType
  {
    IN,
    OUT,
    NONE
  }

  private static class EventEmitter<T>
  {
    private final IndentType _indentType;
    private final Function<T, String> _processor;

    EventEmitter( @Nonnull final IndentType indentType, @Nonnull final Function<T, String> processor )
    {
      _indentType = Objects.requireNonNull( indentType );
      _processor = Objects.requireNonNull( processor );
    }

    @Nonnull
    IndentType getIndentType()
    {
      return _indentType;
    }

    @Nonnull
    Function<T, String> getProcessor()
    {
      return _processor;
    }
  }
}
