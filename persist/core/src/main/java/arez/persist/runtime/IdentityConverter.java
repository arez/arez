package arez.persist.runtime;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

final class IdentityConverter<A extends @Nullable Object> implements Converter<A, A> {
    /**
     * Return the converter that performs no conversions.
     *
     * @param <T> the type of the value.
     * @return the converter.
     */
    @SuppressWarnings("unchecked")
    static <T extends @Nullable Object> Converter<T, T> instance() {
        return (Converter<T, T>) Holder.CONVERTER;
    }

    // Holder class to avoid <clinit> on instance calls
    static final class Holder {
        @NonNull
        static final IdentityConverter<Object> CONVERTER = new IdentityConverter<>();
    }

    @Override
    public A decode(@NonNull final A encoded) {
        return encoded;
    }

    @Override
    public A encode(@NonNull final A value) {
        return value;
    }
}
