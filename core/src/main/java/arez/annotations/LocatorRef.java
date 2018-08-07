package arez.annotations;

import arez.Locator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a template method that returns the {@link Locator} instance for the component.
 * This should only appear on a component if the component has methods annotated with {@link Reference}
 *
 * <p>The method that is annotated with @LocatorRef must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not have any parameters</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link Locator}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
public @interface LocatorRef
{
}
