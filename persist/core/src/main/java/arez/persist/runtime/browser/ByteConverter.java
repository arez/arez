package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Byte as doubles when emitting as json.
 */
final class ByteConverter implements Converter<Byte, Double> {
    @Override
    public Byte decode(@NonNull final Double encoded) {
        return encoded.byteValue();
    }

    @Override
    public Double encode(@NonNull final Byte value) {
        return value.doubleValue();
    }
}
