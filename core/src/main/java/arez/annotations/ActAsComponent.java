package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation added to a class or interface that indicates that the type should be treated as
 * a component when verifying references to type. See {@link ArezComponent#verifyReferencesToComponent()}
 * for additional details.
 */
@Documented
@Target( ElementType.TYPE )
public @interface ActAsComponent
{
}
