package arez.persist.runtime;

import javax.annotation.Nonnull;

/**
 * Converter that encodes a persistent property from application form to encoded form and back again.
 *
 * @param <A> the application form of the value.
 * @param <E> the encoded form of the value.
 */
public interface Converter<A, E>
{
  /**
   * Decode encoded value into form used by the application.
   *
   * @param encoded the encoded form of the value.
   * @return the value.
   */
  A decode( @Nonnull E encoded );

  /**
   * Encode value into form used by the storage system.
   *
   * @param value the decoded value.
   * @return the encoded form of the value.
   */
  E encode( @Nonnull A value );
}
