package org.realityforge.arez.spy;

/**
 * Returning the value of an Observable.
 *
 * @param <T> The type of the Observable value.
 */
@FunctionalInterface
public interface PropertyAccessor<T>
{
  /**
   * Return the value of an Observable.
   *
   * @return the value of an Observable.
   * @throws Throwable if unable to retrieve value.
   */
  T get()
    throws Throwable;
}
