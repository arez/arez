package arez.annotations;

import arez.EqualityComparator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.Nonnull;

/**
 * Annotation applied to a type to declare the default {@link EqualityComparator} used by
 * {@link Observable} and {@link Memoize} members when they do not explicitly specify an
 * {@code equalityComparator}.
 *
 * <p>The annotation processor only considers the exact declared type of the observable or
 * memoized value. It does not walk supertypes, interfaces, array component types, or
 * type-use annotations.</p>
 *
 * <p>An explicit {@code equalityComparator} configured on {@link Observable} or
 * {@link Memoize} always overrides the type-level default.</p>
 */
@Documented
@Target( ElementType.TYPE )
public @interface DefaultEqualityComparator
{
  /**
   * Return the default equality comparator associated with the annotated type.
   *
   * @return the comparator type.
   */
  @Nonnull
  Class<? extends EqualityComparator> value();
}
