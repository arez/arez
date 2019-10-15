package arez.spy;

import grim.annotations.OmitType;

/**
 * Returning the value of an ObservableValue.
 *
 * @param <T> The type of the ObservableValue value.
 */
@OmitType( unless = "arez.enable_property_introspection" )
@FunctionalInterface
public interface PropertyAccessor<T>
{
  /**
   * Return the value of an ObservableValue.
   *
   * @return the value of an ObservableValue.
   * @throws Throwable if unable to retrieve value.
   */
  T get()
    throws Throwable;
}
