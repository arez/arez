package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Integer as doubles when emitting as json.
 */
final class IntegerConverter
  implements Converter<Integer, Double>
{
  @Override
  public Integer decode( @Nonnull final Double encoded )
  {
    return encoded.intValue();
  }

  @Override
  public Double encode( @Nonnull final Integer value )
  {
    return value.doubleValue();
  }
}
