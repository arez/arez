package org.realityforge.arez.api2;

/**
 * The state of the observer relative to the observers dependencies.
 */
public enum ObserverState
{
  /**
   * The observer is not active and is not holding any data about it's dependencies.
   * Typically mean this tracker observer has not been run or if it is a derivation that
   * there is no observer observing the derived observables.
   */
  NOT_TRACKING,
  /**
   * No change since last time observer was notified.
   */
  UP_TO_DATE,
  /**
   * A transitive dependency has changed but it has not been determined if a shallow
   * dependency has changed. The observer will need to check if shallow dependencies
   * have changed. Only Derived observables will propagate POSSIBLY_STALE state.
   */
  POSSIBLY_STALE,
  /**
   * A dependency has changed so the observer will need to recompute.
   */
  STALE
}
