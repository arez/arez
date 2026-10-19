package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Byte as doubles when emitting as json.
 */
final class ByteConverter implements Converter<Byte, Double> {
    @Override
    public Byte decode(final Double encoded) {
        return encoded.byteValue();
    }

    @Override
    public Double encode(final Byte value) {
        return value.doubleValue();
    }
}
