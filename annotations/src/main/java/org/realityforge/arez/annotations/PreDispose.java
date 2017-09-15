package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked prior to disposing element.
 * There should be zero methods annotated with @PreDispose if {@link Container#disposable()} is false,
 * otherwise at most 1 method should be annotated with this annotation.
 *
 * <p>The method that is annotated with @PreDispose must comply with the additional constraints:</p>
 * <ul>
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
public @interface PreDispose
{
}
