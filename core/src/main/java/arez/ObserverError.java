package arez;

import grim.annotations.OmitType;

/**
 * The types of errors that observers can generated.
 */
@OmitType( unless = "arez.enable_observer_error_handlers" )
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
