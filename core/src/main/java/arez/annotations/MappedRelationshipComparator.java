package arez.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Associates a comparator callback with exactly one {@link MappedRelationship}.
 * Unannotated methods named {@code [name]Comparator} are discovered automatically.
 * The method must be a concrete instance method accessible to the generated subclass,
 * and must accept no parameters and return a non-null comparator that can compare target values. The provider runs within each relationship calculation.
 */
@Documented
@Target(ElementType.METHOD)
public @interface MappedRelationshipComparator {
    /**
     * Returns the associated relationship name.
     *
     * @return the relationship name.
     */
    String name();
}
