package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Float as doubles when emitting as json.
 */
final class FloatConverter implements Converter<Float, Double> {
    @Override
    public Float decode(final Double encoded) {
        return encoded.floatValue();
    }

    @Override
    public Double encode(final Float value) {
        return value.doubleValue();
    }
}
