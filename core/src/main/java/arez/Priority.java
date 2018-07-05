package arez;

/**
 * Enum to control scheduling priority of observers/reactions.
 * Observers with higher priorities will react first. If observers have equal priorities then observers
 * scheduled first will react first. Observers must not depend upon ComputedValue instances with
 * a lower priority otherwise priority is ignored.
 */
public enum Priority
{
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
