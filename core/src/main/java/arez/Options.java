package arez;

/**
 * Flags that can be passed when creating observers that determine the configuration of the observer.
 */
public final class Options
{
  /**
   * Flag indicating that the Observer is allowed to observe {@link ComputedValue} instances with a lower priority.
   */
  public static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = 0b10000000000000000000000000000000;
  /**
   * Indicates that the an action can be created from within the Observers tracked function.
   */
  public static final int NESTED_ACTIONS_ALLOWED = 0b01000000000000000000000000000000;
  /**
   * Indicates that the an action must not be created from within the Observers tracked function.
   */
  public static final int NESTED_ACTIONS_DISALLOWED = 0b00100000000000000000000000000000;
  /**
   * Mask to extract "NESTED_ACTIONS" option so can derive default value if required.
   */
  static final int NESTED_ACTIONS_MASK = 0b01100000000000000000000000000000;
  /**
   * Flag set to true if the application code can invoke {@link Observer#reportStale()} to indicate non-arez dependency has changed.
   */
  public static final int MANUAL_REPORT_STALE_ALLOWED = 0b00010000000000000000000000000000;
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
  public static final int PRIORITY_HIGHEST = 0b00000010000000000000000000000000;
  /**
   * High priority.
   * To reduce the chance that downstream elements will react multiple times within a single
   * reaction round, this priority should be used when the observer may trigger many downstream
   * reactions.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_HIGH = 0b00000100000000000000000000000000;
  /**
   * Normal priority if no other priority otherwise specified.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_NORMAL = 0b00000110000000000000000000000000;
  /**
   * Low priority.
   * Usually used to schedule observers that reflect state onto non-reactive
   * application components. i.e. Observers that are used to build html views,
   * perform network operations etc. These reactions are often at low priority
   * to avoid recalculation of dependencies (i.e. {@link ComputedValue}s) triggering
   * this reaction multiple times within a single reaction round.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_LOW = 0b00001000000000000000000000000000;
  /**
   * Lowest priority. Use this priority if the observer is a {@link ComputedValue} that
   * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
   * recomputing state that is likely to either be unobserved or recomputed as part of
   * another observers reaction.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_LOWEST = 0b00001010000000000000000000000000;
  /**
   * Mask used to extract transaction mode bits.
   */
  private static final int TRANSACTION_MASK = 0b00000001100000000000000000000000;
  /**
   * The observer can only read arez state.
   */
  public static final int READ_ONLY = 0b00000001000000000000000000000000;
  /**
   * The observer can read or write arez state.
   */
  public static final int READ_WRITE = 0b00000000100000000000000000000000;
  /**
   * Mask that identifies the bits associated with static configuration.
   */
  static final int OPTIONS_MASK = 0b11111111100000000000000000000000;

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
  static int extractPriority( final int options )
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

  private Options()
  {
  }
}
