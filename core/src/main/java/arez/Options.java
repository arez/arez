package arez;

/**
 * Flags that can be passed when creating observers that determine the configuration of the observer.
 */
public final class Options
{
  /**
   * Flag indicating that the Observer is allowed to observe {@link ComputedValue} instances with a lower priority.
   */
  public static final int OBSERVE_LOWER_PRIORITY_DEPENDENCIES = Flags.OBSERVE_LOWER_PRIORITY_DEPENDENCIES;
  /**
   * Indicates that the an action can be created from within the Observers tracked function.
   */
  public static final int NESTED_ACTIONS_ALLOWED = Flags.NESTED_ACTIONS_ALLOWED;
  /**
   * Indicates that the an action must not be created from within the Observers tracked function.
   */
  public static final int NESTED_ACTIONS_DISALLOWED = Flags.NESTED_ACTIONS_DISALLOWED;
  /**
   * Flag set to true if the application code can invoke {@link Observer#reportStale()} to indicate non-arez dependency has changed.
   */
  public static final int MANUAL_REPORT_STALE_ALLOWED = Flags.MANUAL_REPORT_STALE_ALLOWED;
  /**
   * Highest priority.
   * This priority should be used when the observer will dispose or release other reactive elements
   * (and thus remove elements from being scheduled).
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_HIGHEST = Flags.PRIORITY_HIGHEST;
  /**
   * High priority.
   * To reduce the chance that downstream elements will react multiple times within a single
   * reaction round, this priority should be used when the observer may trigger many downstream
   * reactions.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_HIGH = Flags.PRIORITY_HIGH;
  /**
   * Normal priority if no other priority otherwise specified.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_NORMAL = Flags.PRIORITY_NORMAL;
  /**
   * Low priority.
   * Usually used to schedule observers that reflect state onto non-reactive
   * application components. i.e. Observers that are used to build html views,
   * perform network operations etc. These reactions are often at low priority
   * to avoid recalculation of dependencies (i.e. {@link ComputedValue}s) triggering
   * this reaction multiple times within a single reaction round.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_LOW = Flags.PRIORITY_LOW;
  /**
   * Lowest priority. Use this priority if the observer is a {@link ComputedValue} that
   * may be unobserved when a {@link #PRIORITY_LOW} observer reacts. This is used to avoid
   * recomputing state that is likely to either be unobserved or recomputed as part of
   * another observers reaction.
   * <p>Only one of the PRIORITY_* options should be applied to observer.</p>
   */
  public static final int PRIORITY_LOWEST = Flags.PRIORITY_LOWEST;
  /**
   * The observer can only read arez state.
   * Only one of READ_ONLY and {@link #READ_WRITE} should be specified.
   */
  public static final int READ_ONLY = Flags.READ_ONLY;
  /**
   * The observer can read or write arez state.
   * Only one of {@link #READ_ONLY} and READ_WRITE should be specified.
   */
  public static final int READ_WRITE = Flags.READ_WRITE;
  /**
   * The scheduler will be triggered when the observer is created to immediately invoke the
   * {@link Observer#_tracked} function. This configuration should not be specified if there
   * is no {@link Observer#_tracked} function supplied and {@link #DEFER_REACT} is not specified.
   */
  public static final int REACT_IMMEDIATELY = Flags.REACT_IMMEDIATELY;
  /**
   * The scheduler will not be triggered when the observer is created. The observer either
   * has no {@link Observer#_tracked} function or is responsible for ensuring that
   * {@link ArezContext#triggerScheduler()} is invoked at a later time. This should not be
   * specified if {@link #REACT_IMMEDIATELY} is specified.
   */
  public static final int DEFER_REACT = Flags.DEFER_REACT;
  /**
   * The runtime will keep the observer reacting to dependencies until disposed. This should not be
   * specified if {@link #DEACTIVATE_ON_UNOBSERVE} is specified. This is the default value for observers
   * that supply a tracked function.
   */
  public static final int KEEPALIVE = Flags.KEEPALIVE;
  /**
   * The flag is valid on observers associated with computed values and will deactivate the observer if the
   * computed value has no observers. This should not be specified if {@link #KEEPALIVE} is specified.
   */
  public static final int DEACTIVATE_ON_UNOBSERVE = Flags.DEACTIVATE_ON_UNOBSERVE;

  private Options()
  {
  }
}
