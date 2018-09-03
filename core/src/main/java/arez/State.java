package arez;

import javax.annotation.Nonnull;

/**
 * Constants and methods used to extract runtime state in Observers.
 */
final class State
{
  /**
   * Mask used to extract state bits.
   */
  static final int STATE_MASK = 0b00000000000000000000000000000111;
  /**
   * The observer has been disposed.
   */
  static final int STATE_DISPOSED = 0b00000000000000000000000000000001;
  /**
   * The observer is in the process of being disposed.
   */
  static final int STATE_DISPOSING = 0b00000000000000000000000000000010;
  /**
   * The observer is not active and is not holding any data about it's dependencies.
   * Typically mean this tracker observer has not been run or if it is a ComputedValue that
   * there is no observer observing the associated ObservableValue.
   */
  static final int STATE_INACTIVE = 0b00000000000000000000000000000011;
  /**
   * No change since last time observer was notified.
   */
  static final int STATE_UP_TO_DATE = 0b00000000000000000000000000000100;
  /**
   * A transitive dependency has changed but it has not been determined if a shallow
   * dependency has changed. The observer will need to check if shallow dependencies
   * have changed. Only Derived observables will propagate POSSIBLY_STALE state.
   */
  static final int STATE_POSSIBLY_STALE = 0b00000000000000000000000000000101;
  /**
   * A dependency has changed so the observer will need to recompute.
   */
  static final int STATE_STALE = 0b00000000000000000000000000000110;

  /**
   * Extract and return the observer's state.
   *
   * @param options the options.
   * @return the state.
   */
  static int getState( final int options )
  {
    return options & STATE_MASK;
  }

  /**
   * Return the new value of options when supplied with specified state.
   *
   * @param options the options.
   * @param state   the new state.
   * @return the new options.
   */
  static int setState( final int options, final int state )
  {
    return ( options & ~STATE_MASK ) | state;
  }

  /**
   * Return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   * The inverse of {@link #isNotActive(int)}
   *
   * @param options the options to check.
   * @return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   */
  static boolean isActive( final int options )
  {
    return getState( options ) > STATE_INACTIVE;
  }

  /**
   * Return true if the state is INACTIVE, DISPOSING or DISPOSED.
   * The inverse of {@link #isActive(int)}
   *
   * @param options the options to check.
   * @return true if the state is INACTIVE, DISPOSING or DISPOSED.
   */
  static boolean isNotActive( final int options )
  {
    return !isActive( options );
  }

  /**
   * Return the least stale observer state. if the state is not active
   * then the {@link #STATE_UP_TO_DATE} will be returned.
   *
   * @param options the options to check.
   * @return the least stale observer state.
   */
  static int getLeastStaleObserverState( final int options )
  {
    final int state = getState( options );
    return state > STATE_INACTIVE ? state : STATE_UP_TO_DATE;
  }

  /**
   * Return the state as a string.
   *
   * @param state the state value. One of the STATE_* constants
   * @return the string describing state.
   */
  @Nonnull
  static String getStateName( final int state )
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    switch ( state )
    {
      case STATE_DISPOSED:
        return "DISPOSED";
      case STATE_DISPOSING:
        return "DISPOSING";
      case STATE_INACTIVE:
        return "INACTIVE";
      case STATE_POSSIBLY_STALE:
        return "POSSIBLY_STALE";
      case STATE_STALE:
        return "STALE";
      case STATE_UP_TO_DATE:
        return "UP_TO_DATE";
      default:
        return "UNKNOWN(" + state + ")";
    }
  }

  private State()
  {
  }
}
