package arez.spy;

/**
 * Changing the value of an ObservableValue.
 *
 * @param <T> The type of the ObservableValue value.
 */
@FunctionalInterface
public interface PropertyMutator<T>
{
  /**
   * Change the value of an ObservableValue to specified value.
   *
   * @param value the value of an ObservableValue.
   * @throws Throwable if unable to set value.
   */
  void set( T value )
    throws Throwable;
}
