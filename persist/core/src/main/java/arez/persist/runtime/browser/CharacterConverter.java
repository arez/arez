package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;

/**
 * Represent Character as strings when emitting as json.
 */
final class CharacterConverter implements Converter<Character, String> {
    @Override
    public Character decode(final String encoded) {
        return encoded.charAt(0);
    }

    @Override
    public String encode(final Character value) {
        return value.toString();
    }
}
