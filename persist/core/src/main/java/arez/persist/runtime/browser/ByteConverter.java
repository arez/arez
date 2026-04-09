package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Byte as doubles when emitting as json.
 */
final class ByteConverter
  implements Converter<Byte, Double>
{
  @Override
  public Byte decode( @Nonnull final Double encoded )
  {
    return encoded.byteValue();
  }

  @Override
  public Double encode( @Nonnull final Byte value )
  {
    return value.doubleValue();
  }
}
