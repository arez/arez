package org.realityforge.arez;

import javax.annotation.Nonnull;

/**
 * Interface for interacting with spy system.
 */
public interface Spy
{
  /**
   * Add a spy handler to the list of handlers.
   * The handler should not already be in the list.
   *
   * @param handler the spy handler.
   */
  void addSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Remove spy handler from list of existing handlers.
   * The handler should already be in the list.
   *
   * @param handler the spy handler.
   */
  void removeSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  boolean willPropagateSpyEvents();

  /**
   * Report an event in the Arez system.
   *
   * @param event the event that occurred.
   */
  void reportSpyEvent( @Nonnull Object event );

  /**
   * Return true if there is a transaction active.
   *
   * @return true if there is a transaction active.
   */
  boolean isTransactionActive();
}
