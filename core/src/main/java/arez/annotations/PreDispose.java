package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked prior to disposing element.
 * At most 1 method should be annotated with this annotation.
 * The PreDispose method is the first method invoked during dispose operation and it occurs at the start
 * of the transaction that dispose is occurring within.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * <li>
 *   Should not be public as not expected to be invoked outside the component. A warning will be generated but can
 *   be suppressed by the {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key
 *   "Arez:PublicLifecycleMethod". This warning is also suppressed by the annotation processor if it is implementing
 *   an interface method.
 * </li>
 * <li>
 *   Should not be protected if in the class annotated with the {@link ArezComponent} annotation as the method is not
 *   expected to be invoked outside the component. A warning will be generated but can be suppressed by the
 *   {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:ProtectedLifecycleMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface PreDispose
{
}
