package org.realityforge.arez.api2;

public enum ObserverState
{
  /**
   * Before the observer has been run or outside a batch (if it is a tracking function) or
   * not being observed (if it is observable). The observer is not holding any data about it's
   * dependencies.
   */
  NOT_TRACKING,
  /**
   * No shallow dependency has changed since last computation. The observer will not need to recalculate.
   */
  UP_TO_DATE,
  /**
   * A transitive dependency has changed but it has not been determined if a shallow
   * dependency has changed. The observer will need to check if shallow dependencies
   * have changed.
   *
   * Currently only ComputedValue instances will propagate POSSIBLY_STALE.
   */
  POSSIBLY_STALE,
  /**
   * A shallow dependency has changed so the observer will need to recompute when it is needed.
   */
  STALE
}
