package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Short as doubles when emitting as json.
 */
final class ShortConverter
  implements Converter<Short, Double>
{
  @Override
  public Short decode( @Nonnull final Double encoded )
  {
    return encoded.shortValue();
  }

  @Override
  public Double encode( @Nonnull final Short value )
  {
    return value.doubleValue();
  }
}
