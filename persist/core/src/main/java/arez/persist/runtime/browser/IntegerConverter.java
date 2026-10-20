package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Integer as doubles when emitting as json.
 */
final class IntegerConverter implements Converter<Integer, Double> {
    @Override
    public Integer decode(final Double encoded) {
        return encoded.intValue();
    }

    @Override
    public Double encode(final Integer value) {
        return value.doubleValue();
    }
}
