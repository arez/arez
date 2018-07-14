package arez;

import javax.annotation.Nonnull;
import jsinterop.annotations.JsFunction;

/**
 * Interface that accepts an {@link Observer} that has been scheduled and
 * performs the actions required to run observer.
 */
@FunctionalInterface
@JsFunction
interface Reaction
{
  /**
   * React to changes, or throw an exception if unable to do so.
   *
   * @param observer the observer of changes.
   * @throws Throwable if there is an error reacting to changes.
   */
  void react( @SuppressWarnings( "unusable-by-js" ) @Nonnull Observer observer )
    throws Throwable;
}
