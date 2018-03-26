package arez;

import java.util.Objects;
import javax.annotation.Nullable;

/**
 * Functional interface for detecting if two values are equal.
 *
 * @param <T> The type of the values checked.
 */
@FunctionalInterface
public interface EqualityComparator<T>
{
  /**
   * The default comparator.
   */
  EqualityComparator<?> DEFAULT_COMPARATOR = Objects::equals;

  /**
   * Return a default equality comparator.
   *
   * @param <T> the type to compare.
   * @return the comparator.
   */
  @SuppressWarnings( "unchecked" )
  static <T> EqualityComparator<T> defaultComparator()
  {
    return (EqualityComparator<T>) DEFAULT_COMPARATOR;
  }

  /**
   * Return true if values are considered to be equal, false otherwise.
   *
   * @param value1 the first value.
   * @param value2 the second value.
   * @return true if values are considered to be equal, false otherwise.
   */
  boolean equals( @Nullable T value1, @Nullable T value2 );
}
