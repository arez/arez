package arez.spy;

/**
 * Returning the value of an ObservableValue.
 *
 * @param <T> The type of the ObservableValue value.
 */
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
