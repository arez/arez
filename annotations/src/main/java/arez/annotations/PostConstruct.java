package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked after constructing component.
 * At most 1 method should be annotated with this annotation.
 * The PostConstruct method is invoked after the component classes constructor has been invoked and after all
 * the Arez elements have been constructed but before any @Autorun methods have been scheduled.
 *
 * <p>The method that is annotated with @PostDispose must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must not return a value</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface PostConstruct
{
}
