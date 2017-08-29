package org.realityforge.arez.api2;

/**
 * Interface for performing an action that does not return a value.
 */
@FunctionalInterface
public interface Action
{
  /**
   * Perform an action, or throw an exception if unable to do so.
   *
   * @throws Exception if unable to perform action.
   */
  void call()
    throws Exception;
}
