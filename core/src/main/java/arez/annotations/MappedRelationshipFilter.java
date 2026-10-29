package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Associates a filter callback with exactly one {@link MappedRelationship}.
 * Unannotated methods named {@code filter[TargetType]} are discovered automatically.
 * The method must be a concrete instance method accessible to the generated subclass,
 * and must accept a target and return a primitive boolean. Returning false hides the target without releasing its cache entry or observation.
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationshipFilter {
    /**
     * Returns the associated relationship name.
     *
     * @return the relationship name.
     */
    String name();
}
