package arez.persist.runtime;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A converter that encodes properties for a specific type.
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public final class TypeConverter
{
  /**
   * A map of property names to converters.
   */
  @Nonnull
  private final Map<String, Converter> _converters;

  public TypeConverter( @Nonnull final Map<String, Converter> converters )
  {
    _converters = Objects.requireNonNull( converters );
  }

  /**
   * Decode encoded value into form used by the application.
   *
   * @param key   the property name.
   * @param encoded the encoded form of the value.
   * @return the value.
   * @param <A> the type of the value.
   * @param <E> the type of the encoded value.
   */
  public <A, E> A decode( @Nonnull final String key, @Nullable E encoded )
  {
    if ( null == encoded )
    {
      return null;
    }
    else
    {
      final Converter converter = _converters.get( key );
      return (A) ( null == converter ? encoded : converter.decode( encoded ) );
    }
  }

  /**
   * Encode value into form used by the storage system.
   *
   * @param key   the property name.
   * @param value the decoded value.
   * @return the encoded form of the value.
   * @param <A> the type of the value.
   * @param <E> the type of the encoded value.
   */
  public <A, E> E encode( @Nonnull final String key, @Nullable A value )
  {
    if ( null == value )
    {
      return null;
    }
    else
    {
      final Converter converter = _converters.get( key );
      return (E) ( null == converter ? value : converter.encode( value ) );
    }
  }
}
