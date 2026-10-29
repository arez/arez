package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Associates a retention callback with exactly one {@link MappedRelationship}.
 * Unannotated methods named {@code retain[TargetType]} are discovered automatically.
 * The method must be a concrete instance method accessible to the generated subclass,
 * and must accept a target and return a primitive boolean. Returning true retains a live target whose source is absent, until the predicate changes or the relationship deactivates.
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationshipRetention {
    /**
     * Returns the associated relationship name.
     *
     * @return the relationship name.
     */
    String name();
}
