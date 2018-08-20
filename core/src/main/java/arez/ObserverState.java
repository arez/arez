package arez;

import javax.annotation.Nonnull;

/**
 * The state of the observer relative to the observers dependencies.
 */
enum ObserverState
{
  /**
   * The observer has been disposed.
   */
  DISPOSED,
  /**
   * The observer is in the process of being disposed.
   */
  DISPOSING,
  /**
   * The observer is not active and is not holding any data about it's dependencies.
   * Typically mean this tracker observer has not been run or if it is a ComputedValue that
   * there is no observer observing the associated ObservableValue.
   */
  INACTIVE,
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
  STALE;

  /**
   * Return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   * The inverse of {@link #isNotActive(ObserverState)}
   *
   * @param state the state to check.
   * @return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   */
  static boolean isActive( @Nonnull final ObserverState state )
  {
    return state.ordinal() > INACTIVE.ordinal();
  }

  /**
   * Return true if the state is INACTIVE, DISPOSING or DISPOSED.
   * The inverse of {@link #isActive(ObserverState)}
   *
   * @param state the state to check.
   * @return true if the state is INACTIVE, DISPOSING or DISPOSED.
   */
  static boolean isNotActive( @Nonnull final ObserverState state )
  {
    return !isActive( state );
  }
}
