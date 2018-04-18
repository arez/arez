package arez;

import javax.annotation.Nonnull;
import jsinterop.annotations.JsFunction;

/**
 * Interface that accepts an {@link Observer} that has been scheduled and
 * performs the actions required to run observer.
 *
 * <p>The interface is marked with the {@link JsFunction} annotation to communicate
 * to the GWT2 compiler that it is not necessary to generate class information
 * for the implementing methods which reduces code size somewhat.</p>
 */
@JsFunction
@FunctionalInterface
interface Reaction
{
  /**
   * React to changes, or throw an exception if unable to do so.
   *
   * @param observer the observer of changes.
   * @throws Throwable if there is an error reacting to changes.
   */
  void react( @Nonnull Observer observer )
    throws Throwable;
}
