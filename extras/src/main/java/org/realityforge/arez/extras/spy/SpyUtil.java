package org.realityforge.arez.extras.spy;

import javax.annotation.Nonnull;
import org.realityforge.arez.spy.ActionCompletedEvent;
import org.realityforge.arez.spy.ActionStartedEvent;
import org.realityforge.arez.spy.ComponentCreatedEvent;
import org.realityforge.arez.spy.ComponentDisposedEvent;
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
    if ( ReactionStartedEvent.class == type ||
         TransactionStartedEvent.class == type ||
         ComputeStartedEvent.class == type ||
         ActionStartedEvent.class == type )
    {
      return NestingDelta.INCREASE;
    }
    else if ( ReactionCompletedEvent.class == type ||
              TransactionCompletedEvent.class == type ||
              ComputeCompletedEvent.class == type ||
              ActionCompletedEvent.class == type )
    {
      return NestingDelta.DECREASE;
    }
    else if ( ComponentCreatedEvent.class == type ||
              ComponentDisposedEvent.class == type ||
              ObserverCreatedEvent.class == type ||
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
