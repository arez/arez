package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Long as doubles when emitting as json.
 */
final class LongConverter implements Converter<Long, Double> {
    @Override
    public Long decode(@NonNull final Double encoded) {
        return encoded.longValue();
    }

    @Override
    public Double encode(@NonNull final Long value) {
        return value.doubleValue();
    }
}
