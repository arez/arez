package arez;

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
   * Return true if values are considered to be equal, false otherwise.
   *
   * @param value1 the first value.
   * @param value2 the second value.
   * @return true if values are considered to be equal, false otherwise.
   */
  boolean equals( @Nullable T value1, @Nullable T value2 );
}
