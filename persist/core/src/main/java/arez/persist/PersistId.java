package arez.persist;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation applied to method that returns the a value to identify the instance of the type.
 *
 * <p>The method that is annotated with this annotation must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not have any parameters</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must not be parameterized</li>
 * <li>Must not be private</li>
 * <li>Must not be protected</li>
 * <li>Must not be static</li>
 * <li>If package access, the method must be in the same package as any subclass annotated with the {@link PersistType} annotation.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface PersistId
{
}
