package org.realityforge.arez.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods marked with this annotation are computed values within Arez.
 *
 * <p>The method should take zero parameters, return a single value and throw no
 * exception. The value should be derived from other Observables within the Arez system
 * and the value returned by the method should not change unless the state of the other
 * Observables changes. The method should NOT modify other state in the system.</p>
 *
 * <p>The computed method is automatically wrapped in a READ_ONLY transaction
 * by the annotation parser.</p>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Computed
{
}
