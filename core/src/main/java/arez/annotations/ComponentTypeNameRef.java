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
 * <p>This annotation should appear at most once on a component. The annotation should be on a method that
 * can be overridden, accepts no parameters and returns a String.</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>The method must be accessible to the component subclass which means it must not be package access unless it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentTypeNameRef
{
}
