package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to identify method invoked after disposing element.
 * There should be zero methods annotated with @PreDispose if {@link Container#disposable()} is false,
 * otherwise at most 1 method should be annotated with this annotation.
 *
 * <p>The method that is annotated with @PostDispose must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with {@link Autorun}, {@link ContainerId}, {@link Observable}, {@link Computed}, {@link Action}, {@link javax.annotation.PostConstruct}, {@link PreDispose}, {@link PostDispose}, {@link OnActivate}, {@link OnDeactivate} or {@link OnStale}</li>
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
public @interface PostDispose
{
}
