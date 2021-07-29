package arez.spytools;

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
import javax.annotation.Nonnull;

/**
 * Utility class that simplifies interaction with the Spy subsystem.
 */
public final class SpyUtil
{
  /**
   * The change in nesting.
   */
  public enum NestingDelta
  {
    INCREASE,
    DECREASE,
    NONE,
    UNKNOWN
  }

  private SpyUtil()
  {
  }

  /**
   * Return the changing in nesting this event will cause.
   * Spy events often come in pairs where one event starts a section and another event completes it.
   * This is often to useful to represent as nesting or indentation levels during debug process.
   * This method will return the change in nesting after this event is processed. If the specified
   * nesting is not one of the builtin events then a value of {@link NestingDelta#UNKNOWN} will be
   * returned.
   *
   * @param type the event type.
   * @return the nesting delta.
   */
  @Nonnull
  public static NestingDelta getNestingDelta( @Nonnull final Class<?> type )
  {
    if ( ComponentCreateStartEvent.class == type ||
         ComponentDisposeStartEvent.class == type ||
         ObserveStartEvent.class == type ||
         TransactionStartEvent.class == type ||
         ComputeStartEvent.class == type ||
         TaskStartEvent.class == type ||
         ActionStartEvent.class == type )
    {
      return NestingDelta.INCREASE;
    }
    else if ( ComponentCreateCompleteEvent.class == type ||
              ComponentDisposeCompleteEvent.class == type ||
              ObserveCompleteEvent.class == type ||
              TransactionCompleteEvent.class == type ||
              ComputeCompleteEvent.class == type ||
              TaskCompleteEvent.class == type ||
              ActionCompleteEvent.class == type )
    {
      return NestingDelta.DECREASE;
    }
    else if ( ObserverCreateEvent.class == type ||
              ObserverDisposeEvent.class == type ||
              ObserverErrorEvent.class == type ||
              ObservableValueCreateEvent.class == type ||
              ObservableValueDisposeEvent.class == type ||
              ObservableValueChangeEvent.class == type ||
              ComputableValueActivateEvent.class == type ||
              ComputableValueDeactivateEvent.class == type ||
              ComputableValueCreateEvent.class == type ||
              ComputableValueDisposeEvent.class == type ||
              ObserveScheduleEvent.class == type )
    {
      return NestingDelta.NONE;
    }
    else
    {
      //This is returned when the event type is not a builtin event
      return NestingDelta.UNKNOWN;
    }
  }
}
