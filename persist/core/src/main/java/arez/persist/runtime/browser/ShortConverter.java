package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Short as doubles when emitting as json.
 */
final class ShortConverter implements Converter<Short, Double> {
    @Override
    public Short decode(@NonNull final Double encoded) {
        return encoded.shortValue();
    }

    @Override
    public Double encode(@NonNull final Short value) {
        return value.doubleValue();
    }
}
