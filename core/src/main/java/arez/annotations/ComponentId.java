package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that should return the Id for Arez component.
 *
 * <p>This annotation should appear at most once on a component. The
 * annotation should be on a method that accepts no parameters and returns
 * a non-null value.</p>
 *
 * <p>If this annotation is present, it indicates that the Annotation processor
 * should call this method to get the ID of the component. This ID should be
 * constant and unique (enough) to identify the component. It is used when generating
 * debug names for observables nested within the component. It is also used as the id
 * under which an component is stored when repositories are being generated. If this
 * annotation is not present the Annotation processor will synthesize an ID as a
 * monotonically increasing integer for each instance of the type.</p>
 *
 * <p>It should also be noted that this method will be invoked before the component
 * has been completely constructed as it is used during construction of the reactive
 * elements.</p>
 *
 * <p>The method that is annotated with this annotation must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * <li>Must be accessible to the class annotated by the {@link ArezComponent} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentId
{
}
