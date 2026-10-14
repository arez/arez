package arez.persist.runtime;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Converter that encodes a persistent property from application form to encoded form and back again.
 *
 * @param <A> the application form of the value.
 * @param <E> the encoded form of the value.
 */
public interface Converter<A extends @Nullable Object, E extends @Nullable Object> {
    /**
     * Decode encoded value into form used by the application.
     *
     * @param encoded the encoded form of the value.
     * @return the value.
     */
    @Nullable
    A decode(@NonNull E encoded);

    /**
     * Encode value into form used by the storage system.
     *
     * @param value the decoded value.
     * @return the encoded form of the value.
     */
    @Nullable
    E encode(@NonNull A value);
}
