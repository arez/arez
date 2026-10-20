package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Long as doubles when emitting as json.
 */
final class LongConverter implements Converter<Long, Double> {
    @Override
    public Long decode(final Double encoded) {
        return encoded.longValue();
    }

    @Override
    public Double encode(final Long value) {
        return value.doubleValue();
    }
}
