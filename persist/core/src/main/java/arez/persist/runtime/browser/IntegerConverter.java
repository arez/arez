package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Integer as doubles when emitting as json.
 */
final class IntegerConverter implements Converter<Integer, Double> {
    @Override
    public Integer decode(@NonNull final Double encoded) {
        return encoded.intValue();
    }

    @Override
    public Double encode(@NonNull final Integer value) {
        return value.doubleValue();
    }
}
