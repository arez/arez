package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation added to a class or interface that indicates that the type should be treated as
 * component-like for Arez processor validation.
 *
 * <p>This annotation is not a real {@link ArezComponent} processing entrypoint and does not enable
 * Arez component generation semantics for the annotated type.</p>
 *
 * <p>Instead, it marks the type as a valid host for processor validation paths that accept
 * component-like targets such as misplaced-annotation containment checks, runtime-declared-type
 * validation, component dependency compatibility, and unmanaged component-reference analysis.
 * See {@link ArezComponent#verifyReferencesToComponent()} for additional details.</p>
 */
@Documented
@Target( ElementType.TYPE )
public @interface ArezComponentLike
{
}
