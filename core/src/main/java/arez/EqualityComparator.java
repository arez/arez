package arez;

import javax.annotation.Nullable;

/**
 * Strategy interface used to determine whether two values are equal.
 */
@FunctionalInterface
public interface EqualityComparator
{
  /**
   * Return true if the supplied values are considered equal.
   *
   * @param oldValue the previous value.
   * @param newValue the current value.
   * @return true if values are equal, false otherwise.
   */
  boolean areEqual( @Nullable Object oldValue, @Nullable Object newValue );
}
