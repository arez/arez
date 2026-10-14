package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Float as doubles when emitting as json.
 */
final class FloatConverter implements Converter<Float, Double> {
    @Override
    public Float decode(@NonNull final Double encoded) {
        return encoded.floatValue();
    }

    @Override
    public Double encode(@NonNull final Float value) {
        return value.doubleValue();
    }
}
