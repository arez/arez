package arez.persist.runtime;

import javax.annotation.Nonnull;

final class IdentityConverter<A>
  implements Converter<A, A>
{
  /**
   * Return the converter that performs no conversions.
   *
   * @param <T> the type of the value.
   * @return the converter.
   */
  @SuppressWarnings( "unchecked" )
  static <T> Converter<T, T> instance()
  {
    return (Converter<T, T>) Holder.CONVERTER;
  }

  // Holder class to avoid <clinit> on instance calls
  static final class Holder
  {
    @Nonnull
    static final IdentityConverter<Object> CONVERTER = new IdentityConverter<>();
  }

  @Override
  public A decode( @Nonnull final A encoded )
  {
    return encoded;
  }

  @Override
  public A encode( @Nonnull final A value )
  {
    return value;
  }
}
