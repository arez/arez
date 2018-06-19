package arez;

/**
 * Enum to control scheduling priority of observers/reactions.
 * Observers with higher priorities will react first. If observers have equal priorities then observers
 * scheduled first will react first. Observers must not depend upon ComputedValue instances with
 * a lower priority otherwise priority is ignored.
 *
 * <p>A user should be very careful when specifying a {@link #HIGHEST} or {@link #HIGH} priority as it
 * is possible that the the reaction will be scheduled part way through the process of disposing and/or
 * unlinking one-or-more components. In many cases this may mean invoking
 * <code>Disposable.isDisposed(component)</code> before accessing arez components.</p>
 */
public enum Priority
{
  /**
   * Highest priority.
   * This priority should only be used by reactions responsible for disposing or releasing reactive components.
   * This priority is used when executing the reaction is likely to reduce the load of reactions that may react
   * within the same round. Any reaction scheduled with this priority must guard against accessing partially or
   * wholly disposed reactive elements.
   */
  HIGHEST,
  /**
   * High priority.
   * This priority should be used when the reaction will dispose other reactive elements (and thus they
   * need not be scheduled) or if the reaction will trigger many downstream reactions.
   */
  HIGH,
  /**
   * Normal priority if not otherwise specified.
   */
  NORMAL,
  /**
   * Lowest priority.
   */
  LOW
}
