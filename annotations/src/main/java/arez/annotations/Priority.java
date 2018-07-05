package arez.annotations;

/**
 * Enum to control scheduling priority of observers/reactions.
 * Observers with higher priorities will react first. If observers have equal priorities then observers
 * scheduled first will react first. Observers must not depend upon ComputedValue instances with
 * a lower priority otherwise priority is ignored.
 *
 * <p>A user should be very careful when specifying a {@link #HIGH} priority as it is possible that
 * the the reaction will be scheduled part way through the process of disposing and/or unlinking one-or-more
 * components. Dispose reactions will often be scheduled with a higher priority but reactions unlinking disposed
 * arez components from remaining arez components. In many cases this may mean invoking
 * <code>Disposable.isDisposed(component)</code> before accessing arez components.</p>
 */
public enum Priority
{
  /**
   * Highest user controllable priority.
   * This priority should be used when the reaction will dispose other reactive elements (and thus they
   * need not be scheduled) or if the reaction will trigger many downstream reactions.
   */
  HIGH,
  /**
   * Normal priority if not otherwise specified.
   */
  NORMAL,
  /**
   * Low priority.
   * Usually used to schedule observers that reflect state onto non-reactive
   * application components. i.e. Observers that are used to build html views,
   * perform network operations etc. These reactions are often at low priority
   * to avoid recalculation of dependencies (i.e. {@link Computed}s) triggering
   * this reaction multiple times within a single reaction round.
   */
  LOW,
  /**
   * Lowest priority.
   * This is low-priority reactions that reflect onto non-reactive applications. It is
   * also used for (i.e. {@link Computed}s) that may be unobserved when a {@link #LOW}
   * priority reaction runs.
   */
  LOWEST
}
