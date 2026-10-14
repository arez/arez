package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import org.jspecify.annotations.NonNull;

/**
 * Represent Character as strings when emitting as json.
 */
final class CharacterConverter implements Converter<Character, String> {
    @Override
    public Character decode(@NonNull final String encoded) {
        return encoded.charAt(0);
    }

    @Override
    public String encode(@NonNull final Character value) {
        return value.toString();
    }
}
