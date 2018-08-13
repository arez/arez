package arez.annotations;

/**
 * Defines the multiplicity of the {@link Inverse} relationship for {@link Reference}.
 */
public enum Multiplicity
{
  /**
   * The inverse is related to many references. The type of the inverse must be one of
   * {@link java.util.Collection}, {@link java.util.List} or a {@link java.util.Set} with
   * a type parameter compatible with the class containing the method annotated with the
   * {@link Reference} annotation.
   */
  MANY,
  /**
   * The inverse is related to exactly one reference. The type of the inverse must be
   * compatible with the class containing the method annotated with the {@link Reference}
   * annotation. The inverse MUST be annotated with {@link javax.annotation.Nonnull}
   */
  ONE,
  /**
   * The inverse is related to one or no reference. The type of the inverse must be
   * compatible with the class containing the method annotated with the {@link Reference}
   * annotation. The inverse MUST be annotated with {@link javax.annotation.Nullable}
   */
  ZERO_OR_ONE
}
