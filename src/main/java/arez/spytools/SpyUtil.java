package arez.spytools;

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
import arez.spy.TransactionCompletedEvent;
import arez.spy.TransactionStartedEvent;
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
    if ( ComponentCreateStartedEvent.class == type ||
         ComponentDisposeStartedEvent.class == type ||
         ReactionStartedEvent.class == type ||
         TransactionStartedEvent.class == type ||
         ComputeStartedEvent.class == type ||
         ActionStartedEvent.class == type )
    {
      return NestingDelta.INCREASE;
    }
    else if ( ComponentCreateCompletedEvent.class == type ||
              ComponentDisposeCompletedEvent.class == type ||
              ReactionCompletedEvent.class == type ||
              TransactionCompletedEvent.class == type ||
              ComputeCompletedEvent.class == type ||
              ActionCompletedEvent.class == type )
    {
      return NestingDelta.DECREASE;
    }
    else if ( ObserverCreatedEvent.class == type ||
              ObserverDisposedEvent.class == type ||
              ObserverErrorEvent.class == type ||
              ObservableCreatedEvent.class == type ||
              ObservableDisposedEvent.class == type ||
              ObservableChangedEvent.class == type ||
              ComputedValueActivatedEvent.class == type ||
              ComputedValueDeactivatedEvent.class == type ||
              ComputedValueCreatedEvent.class == type ||
              ComputedValueDisposedEvent.class == type ||
              ReactionScheduledEvent.class == type )
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
