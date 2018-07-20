package arez;

import jsinterop.annotations.JsFunction;

/**
 * Interface for performing an action that does not return a value.
 */
@FunctionalInterface
@JsFunction
public interface SafeProcedure
{
  /**
   * Perform an action.
   */
  void call();
}
