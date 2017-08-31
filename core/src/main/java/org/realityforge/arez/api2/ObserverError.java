package org.realityforge.arez.api2;

/**
 * The types of errors that observers can generated.
 */
public enum ObserverError
{
  /**
   * Exception generated when the reaction is executing.
   */
  REACTION_ERROR,
  /**
   * Exception generated in OnActivate hook action.
   */
  ON_ACTIVATE_ERROR,
  /**
   * Exception generated in OnDeactivate hook action.
   */
  ON_DEACTIVATE_ERROR,
  /**
   * Exception generated in OnStale hook action.
   */
  ON_STALE_ERROR
}
