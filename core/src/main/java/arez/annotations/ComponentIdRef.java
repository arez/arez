package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a template method that returns the Id of the component.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return a value that has the same type as returned by the method annotated by {@link ComponentId} or an <code>int</code>.</li>
 * <li>The method must be accessible to the component subclass which means it must not be package access unless it is in the same package as the arez component.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentIdRef
{
}
