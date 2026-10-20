package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Short as doubles when emitting as json.
 */
final class ShortConverter implements Converter<Short, Double> {
    @Override
    public Short decode(final Double encoded) {
        return encoded.shortValue();
    }

    @Override
    public Double encode(final Short value) {
        return value.doubleValue();
    }
}
