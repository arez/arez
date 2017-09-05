package org.realityforge.arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods marked with this annotation are computed values within Arez.
 *
 * <p>The method should take zero parameters and return a single value. The value
 * should be derived from other {@link org.realityforge.arez.Observable}s
 * within the Arez system and the value returned by the method should not change
 * unless the state of the other Observables changes. The method should NOT modify
 * other state in the system.</p>
 *
 * <p>The computed method is automatically wrapped in a READ_ONLY transaction
 * by the annotation parser.</p>
 */
@Retention( RetentionPolicy.CLASS )
@Target( ElementType.METHOD )
public @interface Computed
{
}
