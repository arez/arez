package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Character as strings when emitting as json.
 */
final class CharacterConverter
  implements Converter<Character, String>
{
  @Override
  public Character decode( @Nonnull final String encoded )
  {
    return encoded.charAt( 0 );
  }

  @Override
  public String encode( @Nonnull final Character value )
  {
    return value.toString();
  }
}
