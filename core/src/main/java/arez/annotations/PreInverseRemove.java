package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Identifies a method that will be invoked before an inverse reference is removed from a component.
 * This method MUST be paired with a method annotated with {@link Inverse} with the same name.
 *
 * <p>If there are multiple methods annotated with this annotation then the methods declared in parent
 * classes will be invoked first and multiple methods within a single class will be invoked in declaration
 * order.</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must have have a single parameter of type that matches the related reference value</li>
 * <li>Must not return a value</li>
 * <li>Must not throw an exception</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the component. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key
 *   "Arez:PublicHookMethod". This warning is also suppressed by the annotation processor if it is implementing
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
public @interface PreInverseRemove
{
  /**
   * Return the name of the Inverse that this method is associated with.
   * This value will be derived if the method name matches the pattern "on[Name]Remove",
   * otherwise it must be specified.
   *
   * @return the name of the Inverse.
   */
  @Nonnull
  String name() default "<default>";
}
