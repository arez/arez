package arez.spy;

import org.jspecify.annotations.Nullable;

/**
 * Changing the value of an ObservableValue.
 *
 * @param <T> The type of the ObservableValue value.
 */
@FunctionalInterface
public interface PropertyMutator<T extends @Nullable Object> {
    /**
     * Change the value of an ObservableValue to specified value.
     *
     * @param value the value of an ObservableValue.
     * @throws Throwable if unable to set value.
     */
    void set(@Nullable T value) throws Throwable;
}
