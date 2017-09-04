package org.realityforge.arez;

/**
 * Interface for performing an action that does not return a value.
 */
@FunctionalInterface
public interface SafeProcedure
{
  /**
   * Perform an action.
   */
  void call();
}
