package arez.spytools.browser;

import akasha.Console;
import akasha.core.JSON;
import arez.Arez;
import arez.spy.ActionCompleteEvent;
import arez.spy.ActionStartEvent;
import arez.spy.ComponentCreateCompleteEvent;
import arez.spy.ComponentCreateStartEvent;
import arez.spy.ComponentDisposeCompleteEvent;
import arez.spy.ComponentDisposeStartEvent;
import arez.spy.ComputableValueActivateEvent;
import arez.spy.ComputableValueCreateEvent;
import arez.spy.ComputableValueDeactivateEvent;
import arez.spy.ComputableValueDisposeEvent;
import arez.spy.ComputeCompleteEvent;
import arez.spy.ComputeStartEvent;
import arez.spy.ObservableValueChangeEvent;
import arez.spy.ObservableValueCreateEvent;
import arez.spy.ObservableValueDisposeEvent;
import arez.spy.ObserveCompleteEvent;
import arez.spy.ObserveScheduleEvent;
import arez.spy.ObserveStartEvent;
import arez.spy.ObserverCreateEvent;
import arez.spy.ObserverDisposeEvent;
import arez.spy.ObserverErrorEvent;
import arez.spy.TaskCompleteEvent;
import arez.spy.TaskStartEvent;
import arez.spy.TransactionCompleteEvent;
import arez.spy.TransactionStartEvent;
import arez.spytools.AbstractSpyEventProcessor;
import arez.spytools.SpyUtil;
import javax.annotation.Nonnull;
import jsinterop.base.Js;

/**
 * A SpyEventHandler that prints spy events to the browser console.
 * The events are grouped according to their nesting levels and are colored to make them easy
 * to digest. This class is designed to be easy to sub-class.
 */
public class ConsoleSpyEventProcessor
  extends AbstractSpyEventProcessor
{
  /*
   * The SVG icons for REACTION_SCHEDULED_COLOR and COMPUTED_COLOR were adjusted variants from the mobx dev tools at https://github.com/andykog/mobx-devtools/blob/master/src/frontend/TabChanges/icons.jsx
   * They were then converted to base64 via http://base64online.org/encode/
   * and converted to excessive css you see below
   */
  @CssRules
  private static final String COMPONENT_COLOR = "color: #410B13; font-weight: normal;";
  @CssRules
  private static final String OBSERVABLE_COLOR = "color: #CF8A3B; font-weight: normal;";
  @CssRules
  private static final String COMPUTED_COLOR =
    "color: #FFBA49; font-weight: normal; background-repeat: no-repeat; background-size: contain; padding-left: 20px; background-image: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNSIgaGVpZ2h0PSIxNSIgdmlld0JveD0iMCAwIDE1IDE1Ij4KICA8ZyBmaWxsPSIjN0I1NkEzIj4KICAgIDxjaXJjbGUgY3g9IjMuNzUiIGN5PSIxMS44MyIgcj0iMiIvPgogICAgPGNpcmNsZSBjeD0iMy43NSIgY3k9IjMuMTciIHI9IjIiLz4KICAgIDxjaXJjbGUgY3g9IjExLjI1IiBjeT0iNy41IiByPSIyIi8+CiAgPC9nPgogIDxnIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzdCNTZBMyIgc3Ryb2tlTWl0ZXJsaW1pdD0iMTAiPgogICAgPHBhdGggZD0iTTYuMjUgNy41bC0yLjUgNC4zMyIvPgogICAgPHBhdGggZD0iTTYuMjUgNy41bC0yLjUtNC4zMyIvPgogICAgPHBhdGggZD0iTTYuMjUgNy41aDUiLz4KICA8L2c+Cjwvc3ZnPgo=);";
  @CssRules
  private static final String OBSERVER_COLOR = "color: #0FA13B; font-weight: normal;";
  @CssRules
  private static final String REACTION_COLOR = "color: #10a210; font-weight: normal;";
  @CssRules
  private static final String REACTION_SCHEDULED_COLOR =
    "color: #10a210; font-weight: normal; background-repeat: no-repeat; background-size: contain; padding-left: 20px; background-image: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNSIgaGVpZ2h0PSIxNSIgdmlld0JveD0iMCAwIDE1IDE1Ij48ZyBmaWxsPSJub25lIiBzdHJva2U9IiMxMGEyMTAiIHN0cm9rZU1pdGVybGltaXQ9IjEwIj48cGF0aCBkPSJNMTIuNjk3IDEwLjVhNiA2IDAgMSAxIC4xMTUtNS43OTIiLz48cGF0aCBkPSJNNy41IDcuNVYzTTcuNSA3LjVMMTAgMTAiLz48L2c+PGcgZmlsbD0iIzAwMDAwMCI+PHBhdGggZD0iTTEwLjYxOCA0Ljc0M0wxMy41IDcuNWwuOTQ3LTMuODc0eiIvPjxjaXJjbGUgY3g9IjcuNSIgY3k9IjcuNSIgcj0iLjc1Ii8+PC9nPjwvc3ZnPgo=);";
  @CssRules
  private static final String TASK_COLOR = "color: #c143eb; font-weight: normal;";
  @CssRules
  private static final String ACTION_COLOR = "color: #006AEB; font-weight: normal;";
  @CssRules
  private static final String TRANSACTION_COLOR = "color: #A18008; font-weight: normal;";
  @CssRules
  private static final String ERROR_COLOR = "color: #A10001; font-weight: normal;";

  /**
   * Create the processor.
   */
  public ConsoleSpyEventProcessor()
  {
    on( ComponentCreateStartEvent.class, this::onComponentCreateStart );
    on( ComponentCreateCompleteEvent.class, this::onComponentCreateCompleteEvent );
    on( ComponentDisposeStartEvent.class, this::onComponentDisposeStart );
    on( ComponentDisposeCompleteEvent.class, this::onComponentDisposeComplete );

    on( ObserverCreateEvent.class, this::onObserverCreate );
    on( ObserverDisposeEvent.class, this::onObserverDispose );
    on( ObserverErrorEvent.class, this::onObserverError );

    on( ObservableValueCreateEvent.class, this::onObservableValueCreate );
    on( ObservableValueDisposeEvent.class, this::onObservableValueDispose );
    on( ObservableValueChangeEvent.class, this::onObservableValueChange );

    on( ComputableValueCreateEvent.class, this::onComputableValueCreate );
    on( ComputableValueActivateEvent.class, this::onComputableValueActivate );
    on( ComputeStartEvent.class, this::onComputeStart );
    on( ComputeCompleteEvent.class, this::onComputeComplete );
    on( ComputableValueDeactivateEvent.class, this::onComputableValueDeactivate );
    on( ComputableValueDisposeEvent.class, this::onComputableValueDispose );

    on( ObserveScheduleEvent.class, this::onObserveSchedule );
    on( ObserveStartEvent.class, this::onObserveStart );
    on( ObserveCompleteEvent.class, this::onObserveComplete );

    on( TransactionStartEvent.class, this::TransactionStart );
    on( TransactionCompleteEvent.class, this::onTransactionComplete );

    on( ActionStartEvent.class, this::onActionStart );
    on( ActionCompleteEvent.class, this::onActionComplete );

    on( TaskStartEvent.class, this::onTaskStart );
    on( TaskCompleteEvent.class, this::onTaskComplete );
  }

  /**
   * Handle the TaskStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onTaskStart( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final TaskStartEvent e )
  {
    log( d, "%cTask Start " + e.getTask().getName(), TASK_COLOR );
  }

  /**
   * Handle the TaskCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onTaskComplete( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final TaskCompleteEvent e )
  {
    log( d, "%cTask Complete " + e.getTask().getName(), TASK_COLOR );
  }

  /**
   * Handle the ComponentCreateStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComponentCreateStart( @Nonnull final SpyUtil.NestingDelta d,
                                         @Nonnull final ComponentCreateStartEvent e )
  {
    log( d, "%cComponent Create Start " + e.getComponentInfo().getName(), COMPONENT_COLOR );
  }

  /**
   * Handle the ComponentCreateCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComponentCreateCompleteEvent( @Nonnull final SpyUtil.NestingDelta d,
                                                 @Nonnull final ComponentCreateCompleteEvent e )
  {
    log( d, "%cComponent Create Complete " + e.getComponentInfo().getName(), COMPONENT_COLOR );
  }

  /**
   * Handle the ComponentDisposeStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComponentDisposeStart( @Nonnull final SpyUtil.NestingDelta d,
                                          @Nonnull final ComponentDisposeStartEvent e )
  {
    log( d, "%cComponent Dispose Start " + e.getComponentInfo().getName(), COMPONENT_COLOR );
  }

  /**
   * Handle the ComponentDisposeCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComponentDisposeComplete( @Nonnull final SpyUtil.NestingDelta d,
                                             @Nonnull final ComponentDisposeCompleteEvent e )
  {
    log( d, "%cComponent Dispose Complete " + e.getComponentInfo().getName(), COMPONENT_COLOR );
  }

  /**
   * Handle the ObserverCreateEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserverCreate( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserverCreateEvent e )
  {
    log( d, "%cObserver Create " + e.getObserver().getName(), OBSERVER_COLOR );
  }

  /**
   * Handle the ObserverDisposeEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserverDispose( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserverDisposeEvent e )
  {
    log( d, "%cObserver Dispose " + e.getObserver().getName(), OBSERVER_COLOR );
  }

  /**
   * Handle the ObserverErrorEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserverError( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserverErrorEvent e )
  {
    log( d,
         "%cObserver Error " + e.getObserver().getName() + " " + e.getError() + " " + e.getThrowable(),
         ERROR_COLOR );
  }

  /**
   * Handle the ObservableCreateEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObservableValueCreate( @Nonnull final SpyUtil.NestingDelta d,
                                          @Nonnull final ObservableValueCreateEvent e )
  {
    log( d, "%cObservable Create " + e.getObservableValue().getName(), OBSERVABLE_COLOR );
  }

  /**
   * Handle the ObservableDisposeEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObservableValueDispose( @Nonnull final SpyUtil.NestingDelta d,
                                           @Nonnull final ObservableValueDisposeEvent e )
  {
    log( d, "%cObservable Dispose " + e.getObservableValue().getName(), OBSERVABLE_COLOR );
  }

  /**
   * Handle the ObservableChangedEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObservableValueChange( @Nonnull final SpyUtil.NestingDelta d,
                                          @Nonnull final ObservableValueChangeEvent e )
  {
    Console.log( "%cObservable Changed " +
                 e.getObservableValue().getName() +
                 ( Arez.arePropertyIntrospectorsEnabled() ? " Value: %o " : null ),
                 OBSERVABLE_COLOR,
                 ( Arez.arePropertyIntrospectorsEnabled() ? e.getValue() : null ) );
  }

  /**
   * Handle the ComputableValueCreateEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputableValueCreate( @Nonnull final SpyUtil.NestingDelta d,
                                          @Nonnull final ComputableValueCreateEvent e )
  {
    log( d, "%cComputed Value Create " + e.getComputableValue().getName(), COMPUTED_COLOR );
  }

  /**
   * Handle the ComputableValueActivateEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputableValueActivate( @Nonnull final SpyUtil.NestingDelta d,
                                            @Nonnull final ComputableValueActivateEvent e )
  {
    log( d, "%cComputed Value Activate " + e.getComputableValue().getName(), COMPUTED_COLOR );
  }

  /**
   * Handle the ComputeStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputeStart( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ComputeStartEvent e )
  {
    log( d, "%cCompute Start " + e.getComputableValue().getName(), COMPUTED_COLOR );
  }

  /**
   * Handle the ComputeCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputeComplete( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ComputeCompleteEvent e )
  {
    log( d, "%cCompute Complete " + e.getComputableValue().getName() + " [" + e.getDuration() + "]", COMPUTED_COLOR );
  }

  /**
   * Handle the ComputableValueDeactivateEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputableValueDeactivate( @Nonnull final SpyUtil.NestingDelta d,
                                              @Nonnull final ComputableValueDeactivateEvent e )
  {
    log( d, "%cComputed Value Deactivate " + e.getComputableValue().getName(), COMPUTED_COLOR );
  }

  /**
   * Handle the ComputableValueDisposeEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onComputableValueDispose( @Nonnull final SpyUtil.NestingDelta d,
                                           @Nonnull final ComputableValueDisposeEvent e )
  {
    log( d, "%cComputed Value Dispose " + e.getComputableValue().getName(), COMPUTED_COLOR );
  }

  /**
   * Handle the ObserveScheduleEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserveSchedule( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserveScheduleEvent e )
  {
    log( d, "%cObserve Scheduled " + e.getObserver().getName(), REACTION_SCHEDULED_COLOR );
  }

  /**
   * Handle the ObserveStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserveStart( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserveStartEvent e )
  {
    log( d, "%cObserve Start " + e.getObserver().getName(), REACTION_COLOR );
  }

  /**
   * Handle the ObserveCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onObserveComplete( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ObserveCompleteEvent e )
  {
    log( d,
         "%cObserve Complete " + e.getObserver().getName() + " [" + e.getDuration() + "]",
         REACTION_COLOR );
  }

  /**
   * Handle the TransactionStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void TransactionStart( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final TransactionStartEvent e )
  {
    log( d, "%cTransaction Start " +
            e.getName() +
            " Mutation=" +
            e.isMutation() +
            " Tracker=" +
            ( e.getTracker() == null ? null : e.getTracker().getName() ),
         TRANSACTION_COLOR );
  }

  /**
   * Handle the TransactionCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onTransactionComplete( @Nonnull final SpyUtil.NestingDelta d,
                                        @Nonnull final TransactionCompleteEvent e )
  {
    log( d, "%cTransaction Complete " +
            e.getName() +
            " Mutation=" +
            e.isMutation() +
            " Tracker=" +
            ( e.getTracker() == null ? null : e.getTracker().getName() ) +
            " [" +
            e.getDuration() +
            "]",
         TRANSACTION_COLOR );
  }

  /**
   * Handle the ActionStartEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onActionStart( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ActionStartEvent e )
  {
    final String message =
      ( e.isTracked() ? "Tracked " : "" ) + "Action Start " + e.getName() +
      parametersToString( e.getParameters() );
    log( d, "%c" + message, ACTION_COLOR );
  }

  /**
   * Handle the ActionCompleteEvent.
   *
   * @param d the change in nesting level.
   * @param e the event.
   */
  protected void onActionComplete( @Nonnull final SpyUtil.NestingDelta d, @Nonnull final ActionCompleteEvent e )
  {
    final String message = ( e.isTracked() ? "Tracked " : "" ) +
                           "Action Complete " +
                           e.getName() +
                           parametersToString( e.getParameters() ) +
                           ( e.returnsResult() && null == e.getThrowable() ? " = " + e.getResult() : "" ) +
                           ( null != e.getThrowable() ? "threw " + e.getThrowable() : "" ) +
                           " Duration [" +
                           e.getDuration() +
                           "]";
    log( d, "%c" + message, ACTION_COLOR );
  }

  /**
   * Convert array of parameters into something consumable by a human.
   * Java objects use toString() while native javascript objects use <code>JSON.stringify()</code>.
   *
   * @param parameters the parameters to convert
   * @return a string representation of the parameters
   */
  @Nonnull
  protected String parametersToString( @Nonnull final Object[] parameters )
  {
    if ( 0 == parameters.length )
    {
      return "()";
    }
    else
    {
      final StringifyReplacer filter = getStringifyReplacer();
      final StringBuilder sb = new StringBuilder();
      sb.append( "(" );
      boolean requireComma = false;
      for ( final Object parameter : parameters )
      {
        if ( requireComma )
        {
          sb.append( ", " );
        }
        else
        {
          requireComma = true;
        }
        sb.append( JSON.stringify( parameter, ( k, value ) -> filter.handleValue( Js.asAny( value ) ) ) );
      }
      sb.append( ")" );
      return sb.toString();
    }
  }

  /**
   * Create replacer callback.
   * This method has been extracted to allow sub classes to override.
   *
   * @return the stringify replacer callback function.
   */
  @Nonnull
  protected StringifyReplacer getStringifyReplacer()
  {
    return new StringifyReplacer();
  }

  /**
   * Log specified message with parameters.
   *
   * @param delta   the nesting delta.
   * @param message the message.
   * @param styling the styling parameter. It is assumed that the message has a %c somewhere in it to identify the start of the styling.
   */
  protected void log( @Nonnull final SpyUtil.NestingDelta delta,
                      @Nonnull final String message,
                      @CssRules @Nonnull final String styling )
  {
    if ( SpyUtil.NestingDelta.INCREASE == delta )
    {
      Console.groupCollapsed( message, styling );
    }
    else if ( SpyUtil.NestingDelta.DECREASE == delta )
    {
      Console.log( message, styling );
      Console.groupEnd();
    }
    else
    {
      Console.log( message, styling );
    }
  }

  @Override
  protected void handleUnhandledEvent( @Nonnull final Object event )
  {
    Console.log( event );
  }
}
