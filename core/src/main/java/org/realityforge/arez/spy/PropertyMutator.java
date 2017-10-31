package org.realityforge.arez.spy;

/**
 * Changing the value of an Observable.
 *
 * @param <T> The type of the Observable value.
 */
@FunctionalInterface
public interface PropertyMutator<T>
{
  /**
   * Change the value of an Observable to specified value.
   *
   * @param value the value of an Observable.
   */
  void set( T value );
}
