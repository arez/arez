package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that will be overridden to provide the "name" of the Arez component.
 * This is useful when the user wants to manually create Arez elements (i.e. {@link arez.ObservableValue} instances,
 * {@link arez.Observer} instances or {@link arez.ComputableValue} instances etc) and wants to use the same naming
 * convention as the generated Arez subclass.
 *
 * <p>This annotation should appear at most once on a component. The
 * annotation should be on a method that accepts no parameters and returns
 * a String.</p>
 *
 * <p>The method that is annotated with the annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 *
 * @see ComponentId
 * @see ComponentTypeNameRef
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentNameRef
{
}
