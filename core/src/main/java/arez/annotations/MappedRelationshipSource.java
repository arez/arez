package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Associates a source stream method with exactly one {@link MappedRelationship}.
 * Unannotated methods named {@code [name]Stream} are discovered automatically.
 * The method must be concrete, accessible to the generated subclass, accept no
 * parameters and return a non-null {@code Stream<Source>}.
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationshipSource {
    /**
     * Returns the associated relationship name.
     *
     * @return the relationship name.
     */
    String name();
}
