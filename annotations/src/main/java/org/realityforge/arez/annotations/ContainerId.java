package org.realityforge.arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate the method that should return the Id for Arez container.
 *
 * <p>This annotation should appear at most once on a container and should
 * not be present if the {@link Container#singleton()} is set to true. The
 * annotation should be on a method that accepts no parameters and returns
 * a non-null value.</p>
 *
 * <p>If this annotation is present, it indicates that the Annotation processor
 * should call this method to get the ID of the container. This ID should be
 * constant and unique (enough) to identify container. It is used when generating
 * debug names for observables nested within the container. If this annotation is
 * not present the Annotation processor will synthesize an ID as a
 * monotonically increasing integer for each instance of the type.</p>
 */
@Documented
@Target( ElementType.METHOD )
public @interface ContainerId
{
}
