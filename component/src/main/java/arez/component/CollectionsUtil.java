package arez.component;

import arez.Arez;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 * Utility methods used when returning results from repositories.
 */
public final class CollectionsUtil
{
  private CollectionsUtil()
  {
  }

  /**
   * Wrap specified parameter with unmodifiable variant if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
   * returns true, otherwise return the supplied list. This method is invoked by the generated code when it detects
   * list properties and should be used by repository extensions when returning collection results.
   *
   * @param <T>        the type of elements in collection.
   * @param collection the input collection.
   * @return the output collection.
   */
  @Nonnull
  public static <T> Collection<T> wrap( @Nonnull final Collection<T> collection )
  {
    return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableCollection( collection ) : collection;
  }

  /**
   * Wrap specified parameter with unmodifiable set if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
   * returns true, otherwise return the supplied set. This method is invoked by the generated code when it detects
   * set properties and should be used by repository extensions when returning set results.
   *
   * @param <T> the type of elements in set.
   * @param set the input set.
   * @return the output set.
   */
  @Nonnull
  public static <T> Set<T> wrap( @Nonnull final Set<T> set )
  {
    return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableSet( set ) : set;
  }

  /**
   * Wrap specified parameter with unmodifiable map if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
   * returns true, otherwise return the supplied map. This method is invoked by the generated code when it detects
   * map properties and should be used by repository extensions when returning map results.
   *
   * @param <K> the type of key elements in map.
   * @param <V> the type of value elements in map.
   * @param map the input map.
   * @return the output map.
   */
  @Nonnull
  public static <K, V> Map<K, V> wrap( @Nonnull final Map<K, V> map )
  {
    return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableMap( map ) : map;
  }

  /**
   * Wrap specified list with unmodifiable list if and only if {@link Arez#areCollectionsPropertiesUnmodifiable()}
   * returns true, otherwise return the supplied list. This method is invoked by the generated code when it detects
   * list properties and should be used by repository extensions when returning list results.
   *
   * @param <T>  the type of elements in collection.
   * @param list the input collection.
   * @return the output collection.
   */
  @Nonnull
  public static <T> List<T> wrap( @Nonnull final List<T> list )
  {
    return Arez.areCollectionsPropertiesUnmodifiable() ? Collections.unmodifiableList( list ) : list;
  }

  /**
   * Convert specified stream to a set, wrapping as an unmodifiable set if required.
   * This method should be called by repository extensions when returning set results.
   *
   * @param <T>    the type of elements in stream.
   * @param stream the input stream.
   * @return the output set
   */
  @Nonnull
  public static <T> Set<T> asSet( @Nonnull final Stream<T> stream )
  {
    return wrap( stream.collect( Collectors.toSet() ) );
  }

  /**
   * Convert specified stream to a list, wrapping as an unmodifiable list if required.
   * This method should be called by repository extensions when returning list results.
   *
   * @param <T>    the type of elements in stream.
   * @param stream the input stream.
   * @return the output list
   */
  @Nonnull
  public static <T> List<T> asList( @Nonnull final Stream<T> stream )
  {
    return wrap( stream.collect( Collectors.toList() ) );
  }
}
