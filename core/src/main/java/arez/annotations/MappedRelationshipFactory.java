package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Associates a target factory method with exactly one {@link MappedRelationship}.
 * Unannotated methods named {@code create[TargetType]} are discovered automatically.
 * The method must be concrete, accessible to the generated subclass, accept a source
 * and return a non-null observable target component. Nullable sources require a
 * factory parameter that accepts null.
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationshipFactory {
    /**
     * Returns the associated relationship name.
     *
     * @return the relationship name.
     */
    String name();
}
