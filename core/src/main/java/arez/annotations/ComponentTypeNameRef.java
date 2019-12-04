package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that will be overridden to return the value of ArezComponent.name().
 * This is only useful when a class potentially has multiple {@link ArezComponent} annotated subclasses.
 * This method is used when manually setting up debug context and you want it to align with the concrete
 * implementations. However often {@link ComponentNameRef} is a better solution. If not specified Arez will
 * generate a private method if needed.
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
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
 *   {@link SuppressWarnings} or {@link SuppressArezWarnings} annotations with a key "Arez:ProtectedRefMethod".
 * </li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentTypeNameRef
{
}
