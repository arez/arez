package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotate the method that will be overridden to return true if the component is in the specified state.
 * This is useful when the component must validate methods are only called in certain states or to change
 * behaviour based on state (i.e. Avoid causing side-effects when disposing).
 *
 * <p>The method that is annotated with this component must comply with the constraints:</p>
 * <ul>
 * <li>May appear zero or more times on a component</li>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a boolean</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the component. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key
 *   "Arez:PublicRefMethod". This warning is also suppressed by the annotation processor if it is implementing
 *   an interface method.
 * </li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link ArezComponent} annotation as the method is not
 *   expected to be invoked outside the component. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:ProtectedMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentStateRef
{
  /**
   * Return the component state which will cause the annotated method to return true.
   *
   * @return the component state which will cause annotated method to return true.
   */
  @Nonnull
  State value() default State.READY;
}
