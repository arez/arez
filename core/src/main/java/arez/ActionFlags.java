package arez;

/**
 * Flags that can be passed to configure actions.
 */
public final class ActionFlags
{
  /**
   * The action can only read arez state.
   */
  public static final int READ_ONLY = 1 << 24;
  /**
   * The action can read or write arez state.
   */
  public static final int READ_WRITE = 1 << 23;
  /**
   * Do not report result to spy infrastructure.
   */
  public static final int NO_REPORT_RESULT = 1 << 12;
  /**
   * The action must create a new transaction and will not use containing transaction.
   */
  public static final int REQUIRE_NEW_TRANSACTION = 1 << 11;
  /**
   * If passed to an action, the the action must verify that an action performed an activity
   * that required a transaction. These activities include:
   * <ul>
   * <li>read or write an observable property.</li>
   * <li>read a computable property.</li>
   * <li>schedule an observer.</li>
   * <li>mark an observer as stale.</li>
   * <li>report possible change in computable property.</li>
   * </ul>
   * <p>This flag must not be present if {@link #NO_VERIFY_ACTION_REQUIRED} is present. If neither
   * VERIFY_ACTION_REQUIRED nor {@link #NO_VERIFY_ACTION_REQUIRED} is specified then VERIFY_ACTION_REQUIRED
   * is assumed.</p>
   */
  public static final int VERIFY_ACTION_REQUIRED = 1 << 27;
  /**
   * This flag can be passed to skip verification that action was required.
   * This flag must not be present if {@link #VERIFY_ACTION_REQUIRED} is present.
   */
  public static final int NO_VERIFY_ACTION_REQUIRED = 1 << 26;
  /**
   * Mask used to extract verify action bits.
   */
  private static final int VERIFY_ACTION_MASK = VERIFY_ACTION_REQUIRED | NO_VERIFY_ACTION_REQUIRED;
  /**
   * Mask containing flags that can be applied to an action.
   */
  static final int CONFIG_FLAGS_MASK =
    READ_ONLY | READ_WRITE | REQUIRE_NEW_TRANSACTION | VERIFY_ACTION_MASK | NO_REPORT_RESULT;

  private ActionFlags()
  {
  }

  static int verifyActionRule( final int flags )
  {
    return Arez.shouldCheckApiInvariants() ?
           0 != ( flags & VERIFY_ACTION_MASK ) ? 0 : VERIFY_ACTION_REQUIRED :
           0;
  }

  /**
   * Return true if flags contains a valid verify action rule.
   *
   * @param flags the flags.
   * @return true if flags contains verify action rule.
   */
  static boolean isVerifyActionRuleValid( final int flags )
  {
    return VERIFY_ACTION_REQUIRED == ( flags & VERIFY_ACTION_MASK ) ||
           NO_VERIFY_ACTION_REQUIRED == ( flags & VERIFY_ACTION_MASK );
  }
}
