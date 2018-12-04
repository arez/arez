package arez;

import javax.annotation.Nonnull;

/**
 * Flags that can be passed when creating observers that determine the configuration of the observer.
 * The class also contains constants and methods used to extract runtime state from Observers.
 */
public final class Flags
{
  /**
   * Flag indicating that the Observer is allowed to observe {@link ComputableValue} instances with a lower priority.
   */
  public static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = 1 << 30;
  /**
   * Indicates that the an action can be created from within the Observers observed function.
   */
  public static final int NESTED_ACTIONS_ALLOWED = 1 << 29;
  /**
   * Indicates that the an action must not be created from within the Observers observed function.
   */
  public static final int NESTED_ACTIONS_DISALLOWED = 1 << 28;
  /**
   * Mask to extract "NESTED_ACTIONS" option so can derive default value if required.
   */
  private static final int NESTED_ACTIONS_MASK = NESTED_ACTIONS_ALLOWED | NESTED_ACTIONS_DISALLOWED;
  /**
   * Flag set set if the application code can not invoke {@link Observer#reportStale()} or {@link ComputableValue#reportPossiblyChanged()} to
   * indicate dependency has changed and the observer.
   *
   * @see arez.annotations.DepType#AREZ
   */
  public static final int AREZ_DEPENDENCIES = 1 << 27;
  /**
   * Flag set set if the application code can not invoke {@link Observer#reportStale()} or {@link ComputableValue#reportPossiblyChanged()} to
   * indicate dependency has changed. It is not necessary for the observer to invoke  {@link ObservableValue#reportObserved()} on any dependency.
   *
   * @see arez.annotations.DepType#AREZ_OR_NONE
   */
  public static final int AREZ_OR_NO_DEPENDENCIES = 1 << 26;
  /**
   * Flag set if the application code can invoke {@link Observer#reportStale()} or {@link ComputableValue#reportPossiblyChanged()} to indicate non-arez dependency has changed.
   *
   * @see arez.annotations.DepType#AREZ_OR_EXTERNAL
   */
  public static final int AREZ_OR_EXTERNAL_DEPENDENCIES = 1 << 25;
  /**
   * Mask used to extract dependency type bits.
   */
  private static final int DEPENDENCIES_TYPE_MASK =
    AREZ_DEPENDENCIES | AREZ_OR_NO_DEPENDENCIES | AREZ_OR_EXTERNAL_DEPENDENCIES;
  /**
   * The observer can only read arez state.
   */
  public static final int READ_ONLY = 1 << 24;
  /**
   * The observer can read or write arez state.
   */
  public static final int READ_WRITE = 1 << 23;
  /**
   * Mask used to extract transaction mode bits.
   */
  private static final int TRANSACTION_MASK = READ_ONLY | READ_WRITE;
  /**
   * The scheduler will be triggered when the observer is created to immediately invoke the
   * {@link Observer#_observe} function. This configuration should not be specified if there
   * is no {@link Observer#_observe} function supplied. This should not be
   * specified if {@link #RUN_LATER} is specified.
   */
  @SuppressWarnings( "WeakerAccess" )
  public static final int RUN_NOW = Task.Flags.RUN_NOW;
  /**
   * The scheduler will not be triggered when the observer is created. The observer either
   * has no {@link Observer#_observe} function or is responsible for ensuring that
   * {@link ArezContext#triggerScheduler()} is invoked at a later time. This should not be
   * specified if {@link #RUN_NOW} is specified.
   */
  public static final int RUN_LATER = Task.Flags.RUN_LATER;
  /**
   * The runtime will keep the observer reacting to dependencies until disposed. This is the default value for
   * observers that supply a observed function but may be explicitly supplied when creating {@link ComputableValue}
   * instances.
   */
  public static final int KEEPALIVE = 1 << 20;
  /**
   * The flag is valid on observers associated with computable values and will deactivate the observer if the
   * computable value has no observers.
   */
  static final int DEACTIVATE_ON_UNOBSERVE = 1 << 19;
  /**
   * The flag is valid on observers where the observed function is invoked by the application.
   */
  static final int APPLICATION_EXECUTOR = 1 << 18;
  /**
   * Mask used to extract react type bits.
   */
  private static final int SCHEDULE_TYPE_MASK = KEEPALIVE | DEACTIVATE_ON_UNOBSERVE | APPLICATION_EXECUTOR;
  /**
   * A flag that can be passed to a Task that indicates that the task should be disposed after it has completed execution.
   */
  public static final int DISPOSE_ON_COMPLETE = Task.Flags.DISPOSE_ON_COMPLETE;
  /**
   * Highest priority.
   * This priority should be used when the observer will dispose or release other reactive elements
   * (and thus remove elements from being scheduled).
   * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
   *
   * @see arez.annotations.Priority#HIGHEST
   * @see arez.spy.Priority#HIGHEST
   */
  public static final int PRIORITY_HIGHEST = Task.Flags.PRIORITY_HIGHEST;
  /**
   * High priority.
   * To reduce the chance that downstream elements will react multiple times within a single
   * reaction round, this priority should be used when the observer may trigger many downstream
   * reactions.
   * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
   *
   * @see arez.annotations.Priority#HIGH
   * @see arez.spy.Priority#HIGH
   */
  public static final int PRIORITY_HIGH = Task.Flags.PRIORITY_HIGH;
  /**
   * Normal priority if no other priority otherwise specified.
   * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
   *
   * @see arez.annotations.Priority#NORMAL
   * @see arez.spy.Priority#NORMAL
   */
  public static final int PRIORITY_NORMAL = Task.Flags.PRIORITY_NORMAL;
  /**
   * Low priority.
   * Usually used to schedule observers that reflect state onto non-reactive
   * application components. i.e. Observers that are used to build html views,
   * perform network operations etc. These reactions are often at low priority
   * to avoid recalculation of dependencies (i.e. {@link ComputableValue}s) triggering
   * this reaction multiple times within a single reaction round.
   * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
   *
   * @see arez.annotations.Priority#LOW
   * @see arez.spy.Priority#LOW
   */
  public static final int PRIORITY_LOW = Task.Flags.PRIORITY_LOW;
  /**
   * Lowest priority. Use this priority if the observer is a {@link ComputableValue} that
   * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
   * recomputing state that is likely to either be unobserved or recomputed as part of
   * another observers reaction.
   * <p>Only one of the PRIORITY_* flags should be applied to observer.</p>
   *
   * @see arez.annotations.Priority#LOWEST
   * @see arez.spy.Priority#LOWEST
   */
  public static final int PRIORITY_LOWEST = Task.Flags.PRIORITY_LOWEST;
  /**
   * The flag can be passed to actions or observers to force the action to not report result to spy infrastructure.
   */
  public static final int NO_REPORT_RESULT = 1 << 12;
  /**
   * Mask that identifies the bits associated with static configuration.
   */
  static final int CONFIG_FLAGS_MASK =
    OBSERVE_LOWER_PRIORITY_DEPENDENCIES |
    NESTED_ACTIONS_MASK |
    DEPENDENCIES_TYPE_MASK |
    TRANSACTION_MASK |
    Task.Flags.RUN_TYPE_MASK |
    SCHEDULE_TYPE_MASK |
    Task.Flags.PRIORITY_MASK |
    NO_REPORT_RESULT;
  /**
   * The flag can be passed to actions to force the action to create a new transaction.
   */
  public static final int REQUIRE_NEW_TRANSACTION = 1 << 11;
  /**
   * The flag can be passed to actions to force the action to verify that an action performed an activity
   * that required an action. These activities include:
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
  public static final int VERIFY_ACTION_REQUIRED = AREZ_DEPENDENCIES;
  /**
   * This flag can be passed to skip verification that action was required.
   * This flag must not be present if {@link #VERIFY_ACTION_REQUIRED} is present.
   */
  public static final int NO_VERIFY_ACTION_REQUIRED = AREZ_OR_NO_DEPENDENCIES;
  /**
   * Mask used to extract verify action bits.
   */
  private static final int VERIFY_ACTION_MASK = VERIFY_ACTION_REQUIRED | NO_VERIFY_ACTION_REQUIRED;
  /**
   * Mask containing flags that can be applied to an action.
   */
  static final int ACTION_FLAGS_MASK =
    TRANSACTION_MASK | REQUIRE_NEW_TRANSACTION | VERIFY_ACTION_MASK | NO_REPORT_RESULT;
  /**
   * The flag is currently unused.
   */
  @SuppressWarnings( "unused" )
  static final int UNUSED1 = 1 << 14;
  /**
   * The flag is currently unused.
   */
  @SuppressWarnings( "unused" )
  static final int UNUSED2 = 1 << 13;
  /**
   * The flag is currently unused.
   */
  @SuppressWarnings( "unused" )
  static final int UNUSED3 = 1 << 10;
  /**
   * Flag indicating whether next scheduled invocation of {@link Observer} should invoke {@link Observer#_observe} or {@link Observer#_onDepsChange}.
   */
  static final int EXECUTE_OBSERVE_NEXT = 1 << 9;
  /**
   * Mask used to extract state bits.
   * State is the lowest bits as it is the most frequently accessed numeric fields
   * and placing values at lower part of integer avoids a shift.
   */
  private static final int STATE_MASK = 0b111;
  /**
   * The observer has been disposed.
   */
  static final int STATE_DISPOSED = 0b001;
  /**
   * The observer is in the process of being disposed.
   */
  static final int STATE_DISPOSING = 0b010;
  /**
   * The observer is not active and is not holding any data about it's dependencies.
   * Typically mean this tracker observer has not been run or if it is a ComputableValue that
   * there is no observer observing the associated ObservableValue.
   */
  static final int STATE_INACTIVE = 0b011;
  /**
   * No change since last time observer was notified.
   */
  static final int STATE_UP_TO_DATE = 0b100;
  /**
   * A transitive dependency has changed but it has not been determined if a shallow
   * dependency has changed. The observer will need to check if shallow dependencies
   * have changed. Only Derived observables will propagate POSSIBLY_STALE state.
   */
  static final int STATE_POSSIBLY_STALE = 0b101;
  /**
   * A dependency has changed so the observer will need to recompute.
   */
  static final int STATE_STALE = 0b110;
  /**
   * Mask that identifies the bits associated with runtime configuration.
   */
  static final int RUNTIME_FLAGS_MASK = EXECUTE_OBSERVE_NEXT | STATE_MASK;

  static int nestedActionRule( final int flags )
  {
    return Arez.shouldCheckInvariants() ?
           defaultFlagUnlessSpecified( flags, NESTED_ACTIONS_MASK, NESTED_ACTIONS_DISALLOWED ) :
           0;
  }

  static int verifyActionRule( final int flags )
  {
    return Arez.shouldCheckApiInvariants() ?
           defaultFlagUnlessSpecified( flags, VERIFY_ACTION_MASK, VERIFY_ACTION_REQUIRED ) :
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

  /**
   * Return true if flags contains a valid nested action mode.
   *
   * @param flags the flags.
   * @return true if flags contains valid nested action mode.
   */
  static boolean isNestedActionsModeValid( final int flags )
  {
    return NESTED_ACTIONS_ALLOWED == ( flags & NESTED_ACTIONS_ALLOWED ) ^
           NESTED_ACTIONS_DISALLOWED == ( flags & NESTED_ACTIONS_DISALLOWED );
  }

  /**
   * Return true if flags contains transaction mode.
   *
   * @param flags the flags.
   * @return true if flags contains transaction mode.
   */
  static boolean isTransactionModeSpecified( final int flags )
  {
    return 0 != ( flags & TRANSACTION_MASK );
  }

  static int transactionMode( final int flags )
  {
    return Arez.shouldEnforceTransactionType() ?
           defaultFlagUnlessSpecified( flags, TRANSACTION_MASK, READ_ONLY ) :
           0;
  }

  /**
   * Return true if flags contains a valid transaction mode.
   *
   * @param flags the flags.
   * @return true if flags contains transaction mode.
   */
  static boolean isTransactionModeValid( final int flags )
  {
    return 0 != ( flags & READ_ONLY ) ^ 0 != ( flags & READ_WRITE );
  }

  /**
   * Return the default dependency type flag if dependency type not specified.
   *
   * @param flags the flags.
   * @return the default dependency type if dependency type unspecified else 0.
   */
  static int dependencyType( final int flags )
  {
    return Arez.shouldCheckInvariants() ?
           defaultFlagUnlessSpecified( flags, DEPENDENCIES_TYPE_MASK, AREZ_DEPENDENCIES ) :
           0;
  }

  /**
   * Return name of transaction mode.
   *
   * @param flags the flags.
   * @return true if flags contains transaction mode.
   */
  @Nonnull
  static String getTransactionModeName( final int flags )
  {
    assert Arez.shouldCheckInvariants() || Arez.shouldCheckApiInvariants();
    if ( 0 != ( flags & READ_ONLY ) )
    {
      return "READ_ONLY";
    }
    else if ( 0 != ( flags & READ_WRITE ) )
    {
      return "READ_WRITE";
    }
    else
    {
      return "UNKNOWN(" + flags + ")";
    }
  }

  /**
   * Extract and return the schedule type.
   *
   * @param flags the flags.
   * @return the schedule type.
   */
  static int getScheduleType( final int flags )
  {
    return flags & SCHEDULE_TYPE_MASK;
  }

  /**
   * Return true if flags contains a valid ScheduleType.
   *
   * @param flags the flags.
   * @return true if flags contains a valid ScheduleType.
   */
  static boolean isScheduleTypeValid( final int flags )
  {
    return KEEPALIVE == ( flags & KEEPALIVE ) ^
           DEACTIVATE_ON_UNOBSERVE == ( flags & DEACTIVATE_ON_UNOBSERVE ) ^
           APPLICATION_EXECUTOR == ( flags & APPLICATION_EXECUTOR );
  }

  /**
   * Extract and return the observer's state.
   *
   * @param flags the flags.
   * @return the state.
   */
  static int getState( final int flags )
  {
    return flags & STATE_MASK;
  }

  /**
   * Return the new value of flags when supplied with specified state.
   *
   * @param flags the flags.
   * @param state the new state.
   * @return the new flags.
   */
  static int setState( final int flags, final int state )
  {
    return ( flags & ~STATE_MASK ) | state;
  }

  /**
   * Return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   * The inverse of {@link #isNotActive(int)}
   *
   * @param flags the flags to check.
   * @return true if the state is UP_TO_DATE, POSSIBLY_STALE or STALE.
   */
  static boolean isActive( final int flags )
  {
    return getState( flags ) > STATE_INACTIVE;
  }

  /**
   * Return true if the state is INACTIVE, DISPOSING or DISPOSED.
   * The inverse of {@link #isActive(int)}
   *
   * @param flags the flags to check.
   * @return true if the state is INACTIVE, DISPOSING or DISPOSED.
   */
  static boolean isNotActive( final int flags )
  {
    return !isActive( flags );
  }

  /**
   * Return the least stale observer state. if the state is not active
   * then the {@link #STATE_UP_TO_DATE} will be returned.
   *
   * @param flags the flags to check.
   * @return the least stale observer state.
   */
  static int getLeastStaleObserverState( final int flags )
  {
    final int state = getState( flags );
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

  /**
   * Return the default flag unless a value in mask is specified.
   *
   * @param flags       the flags.
   * @param mask        the mask.
   * @param defaultFlag the default flag to apply
   * @return the default flag unless flag has value.
   */
  private static int defaultFlagUnlessSpecified( final int flags, final int mask, final int defaultFlag )
  {
    return 0 != ( flags & mask ) ? 0 : defaultFlag;
  }
}
