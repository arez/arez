package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Float as doubles when emitting as json.
 */
final class FloatConverter
  implements Converter<Float, Double>
{
  @Override
  public Float decode( @Nonnull final Double encoded )
  {
    return encoded.floatValue();
  }

  @Override
  public Double encode( @Nonnull final Float value )
  {
    return value.doubleValue();
  }
}
