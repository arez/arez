package org.realityforge.arez.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
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
import org.realityforge.arez.spy.TransactionCompletedEvent;
import org.realityforge.arez.spy.TransactionStartedEvent;

final class ExamplesSpyUtil
{
  private static final Map<Class, EventEmitter> _emitterMap = new HashMap<>();
  private static int _indentLevel;

  private ExamplesSpyUtil()
  {
  }

  static
  {
    emitter( ObserverCreatedEvent.class,
             e -> "Observer Created " + e.getObserver().getName() );
    emitter( ObserverCreatedEvent.class,
             e -> "Observer Created " + e.getObserver().getName() );
    emitter( ObserverDisposedEvent.class,
             e -> "Observer Disposed " + e.getObserver().getName() );
    emitter( ObserverErrorEvent.class,
             e -> "Observer Error " + e.getObserver().getName() + " " + e.getError() + " " + e.getThrowable() );
    emitter( ObservableCreatedEvent.class,
             e -> "Observable Created " + e.getObservable().getName() );
    emitter( ObservableDisposedEvent.class,
             e -> "Observable Disposed " + e.getObservable().getName() );
    emitter( ObservableChangedEvent.class,
             e -> "Observable Changed " + e.getObservable().getName() );
    emitter( ComputedValueActivatedEvent.class,
             e -> "Computed Value Activate " + e.getComputedValue().getName() );
    emitter( ComputedValueDeactivatedEvent.class,
             e -> "Computed Value Deactivate " + e.getComputedValue().getName() );
    emitter( ComputedValueCreatedEvent.class,
             e -> "Computed Value Created " + e.getComputedValue().getName() );
    emitter( ComputedValueDisposedEvent.class,
             e -> "Computed Value Disposed " + e.getComputedValue().getName() );
    emitter( ReactionStartedEvent.class,
             e -> "Reaction Started " + e.getObserver().getName() );
    emitter( ReactionScheduledEvent.class,
             e -> "Reaction Scheduled " + e.getObserver().getName() );
    emitter( ReactionCompletedEvent.class,
             e -> "Reaction Completed " + e.getObserver().getName() + " [" + e.getDuration() + "]" );
    emitter( TransactionStartedEvent.class,
             e -> "Transaction Started " +
                  e.getName() +
                  " Mutation=" +
                  e.isMutation() +
                  " Tracker=" +
                  ( e.getTracker() == null ? null : e.getTracker().getName() ) );
    emitter( TransactionCompletedEvent.class,
             e -> "Transaction Completed " +
                  e.getName() +
                  " Mutation=" +
                  e.isMutation() +
                  " Tracker=" +
                  ( e.getTracker() == null ? null : e.getTracker().getName() ) + " [" + e.getDuration() + "]" );
    emitter( ComputeStartedEvent.class,
             e -> "Compute Started " + e.getComputedValue().getName() );
    emitter( ComputeCompletedEvent.class,
             e -> "Compute Completed " + e.getComputedValue().getName() + " [" + e.getDuration() +
                  "]" );

    emitter( ActionStartedEvent.class,
             e -> ( e.isTracked() ? "Tracked " : "" ) + "Action Started " +
                  e.getName() +
                  "(" +
                  Arrays.toString( e.getParameters() ) +
                  ")" );
    emitter( ActionCompletedEvent.class,
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

  private static <T> void emitter( @Nonnull final Class<T> type,
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

      System.out.println( sb.toString() );
      _indentLevel += SpyUtil.NestingDelta.INCREASE == delta ? 1 : 0;
    }
    else
    {
      System.out.println( event );
    }
  }

  private static class EventEmitter<T>
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
