package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;
import org.realityforge.anodoc.Unsupported;

/**
 * Marks a template method that returns the {@link org.realityforge.arez.ArezContext} instance for component.
 *
 * <p>The method that is annotated with @ContextRef must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link org.realityforge.arez.ArezContext}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
@Unsupported( "This is largely experimental and intended for framework users rather than casual users of the framework" )
public @interface ContextRef
{
}
