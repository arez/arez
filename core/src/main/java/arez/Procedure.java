package arez;

import jsinterop.annotations.JsFunction;

/**
 * Interface for performing an action that does not return a value.
 */
@FunctionalInterface
@JsFunction
public interface Procedure
{
  /**
   * Perform an action, or throw an exception if unable to do so.
   *
   * @throws Throwable if unable to perform action.
   */
  void call()
    throws Throwable;
}
