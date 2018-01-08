package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.realityforge.anodoc.Unsupported;

/**
 * Marks a template method that returns the {@link arez.Component} instance for the component.
 *
 * <p>The method that is annotated with @ComponentRef must also comply with the following constraints:</p>
 * <ul>
 * <li>Must not be annotated with any other arez annotation</li>
 * <li>Must not be private</li>
 * <li>Must not be static</li>
 * <li>Must not be final</li>
 * <li>Must not be abstract</li>
 * <li>Must not throw any exceptions</li>
 * <li>Must return an instance of {@link arez.Component}.</li>
 * </ul>
 */
@Documented
@Target( ElementType.METHOD )
@Unsupported( "This is largely experimental and intended for framework users rather than casual users of the framework" )
public @interface ComponentRef
{
}
