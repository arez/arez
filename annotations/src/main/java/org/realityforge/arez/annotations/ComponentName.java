package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that will be overridden to provider the debug name for the Arez component.
 * This is useful when the user wants to manually create Arez elements (i.e. Observables,
 * Autoruns, Computeds etc) and wants to use the same naming convention as the generated Arez
 * subclass. If not specified Arez will generate a private method (currently named <tt>$$arez$$_id()</tt>)
 * that serves the same purpose. The method returns a string name for the model if names are enabled.
 *
 * <p>This annotation should appear at most once on a component and should
 * not be present if the {@link ArezComponent#singleton()} is set to true. The
 * annotation should be on a method that accepts no parameters and returns
 * a String.</p>
 *
 * <p>The method that is annotated with @ComponentName must comply with the additional constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must have 0 parameters</li>
 * <li>Must return a String</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw exceptions</li>
 * </ul>
 * @see ComponentId
 * @see ComponentTypeName
 */
@Documented
@Target( ElementType.METHOD )
public @interface ComponentName
{
}
