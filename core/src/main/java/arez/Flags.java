package arez;

import javax.annotation.Nonnull;

/**
 * Constants and methods used to extract runtime state in Observers.
 */
final class Flags
{
  /**
   * Flag indicating that the Observer is allowed to observe {@link ComputedValue} instances with a lower priority.
   */
  static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = 0b10000000000000000000000000000000;
  /**
   * Indicates that the an action can be created from within the Observers tracked function.
   */
  static final int NESTED_ACTIONS_ALLOWED = 0b01000000000000000000000000000000;
  /**
   * Indicates that the an action must not be created from within the Observers tracked function.
   */
  static final int NESTED_ACTIONS_DISALLOWED = 0b00100000000000000000000000000000;
  /**
   * Mask to extract "NESTED_ACTIONS" option so can derive default value if required.
   */
  private static final int NESTED_ACTIONS_MASK = 0b01100000000000000000000000000000;
  /**
   * Flag set to true if the application code can invoke {@link Observer#reportStale()} to indicate non-arez dependency has changed.
   */
  static final int MANUAL_REPORT_STALE_ALLOWED = 0b00010000000000000000000000000000;
  /**
   * Mask used to extract priority bits.
   */
  private static final int PRIORITY_MASK = 0b00001110000000000000000000000000;
  /**
   * Shift used to extract priority after applying mask.
   */
  private static final int PRIORITY_SHIFT = 25;
  /**
   * Highest priority.
   * This priority should be used when the observer will dispose or release other reactive elements
   * (and thus remove elements from being scheduled).
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  static final int PRIORITY_HIGHEST = 0b00000010000000000000000000000000;
  /**
   * High priority.
   * To reduce the chance that downstream elements will react multiple times within a single
   * reaction round, this priority should be used when the observer may trigger many downstream
   * reactions.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  static final int PRIORITY_HIGH = 0b00000100000000000000000000000000;
  /**
   * Normal priority if no other priority otherwise specified.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  static final int PRIORITY_NORMAL = 0b00000110000000000000000000000000;
  /**
   * Low priority.
   * Usually used to schedule observers that reflect state onto non-reactive
   * application components. i.e. Observers that are used to build html views,
   * perform network operations etc. These reactions are often at low priority
   * to avoid recalculation of dependencies (i.e. {@link ComputedValue}s) triggering
   * this reaction multiple times within a single reaction round.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  static final int PRIORITY_LOW = 0b00001000000000000000000000000000;
  /**
   * Lowest priority. Use this priority if the observer is a {@link ComputedValue} that
   * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
   * recomputing state that is likely to either be unobserved or recomputed as part of
   * another observers reaction.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  static final int PRIORITY_LOWEST = 0b00001010000000000000000000000000;
  /**
   * Mask used to extract transaction mode bits.
   */
  private static final int TRANSACTION_MASK = 0b00000001100000000000000000000000;
  /**
   * The observer can only read arez state.
   */
  static final int READ_ONLY = 0b00000001000000000000000000000000;
  /**
   * The observer can read or write arez state.
   */
  static final int READ_WRITE = 0b00000000100000000000000000000000;
  /**
   * Mask that identifies the bits associated with static configuration.
   */
  static final int CONFIG_FLAGS_MASK = 0b11111111100000000000000000000000;
  /**
   * Flag indicating whether next scheduled invocation of {@link Observer} should invokeReaction {@link Observer#_tracked} or {@link Observer#_onDepsUpdated}.
   */
  static final int EXECUTE_TRACKED_NEXT = 0b00000000000000000000000000010000;
  /**
   * The observer has been scheduled.
   */
  static final int SCHEDULED = 0b00000000000000000000000000001000;
  /**
   * Mask used to extract state bits.
   * State is the lowest bits as it is the most frequently accessed numeric fields
   * and placing values at lower part of integer avoids a shift.
   */
  private static final int STATE_MASK = 0b00000000000000000000000000000111;
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
   * Mask that identifies the bits associated with runtime configuration.
   */
  static final int RUNTIME_FLAGS_MASK = 0b00000000000000000000000000011111;

  /**
   * Return true if options contains priority.
   *
   * @param options the options.
   * @return true if options contains priority.
   */
  static boolean isPrioritySpecified( final int options )
  {
    return 0 != ( options & PRIORITY_MASK );
  }

  /**
   * Extract and return the priority value ranging from the highest priority 0 and lowest priority 4.
   * This method assumes that options has valid priority and will not attempt to re-check.
   *
   * @param options the options.
   * @return the priority.
   */
  static int getPriority( final int options )
  {
    return ( ( options & PRIORITY_MASK ) >> PRIORITY_SHIFT ) - 1;
  }

  /**
   * Return true if options contains transaction mode.
   *
   * @param options the options.
   * @return true if options contains transaction mode.
   */
  static boolean isTransactionModeSpecified( final int options )
  {
    return 0 != ( options & TRANSACTION_MASK );
  }

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

  private Flags()
  {
  }
}
