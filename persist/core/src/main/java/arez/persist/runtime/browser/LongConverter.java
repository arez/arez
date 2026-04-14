package arez.persist.runtime.browser;

import arez.persist.runtime.Converter;
import javax.annotation.Nonnull;

/**
 * Represent Long as doubles when emitting as json.
 */
final class LongConverter
  implements Converter<Long, Double>
{
  @Override
  public Long decode( @Nonnull final Double encoded )
  {
    return encoded.longValue();
  }

  @Override
  public Double encode( @Nonnull final Long value )
  {
    return value.doubleValue();
  }
}
