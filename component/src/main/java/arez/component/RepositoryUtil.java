package arez.component;

import arez.Arez;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

/**
 * Utility methods used when returning results from repositories.
 */
public final class RepositoryUtil
{
  private RepositoryUtil()
  {
  }

  /**
   * Convert specified list to results to return from query.
   * The result list will be wrapped in an unmodifiable list if {@link Arez#areRepositoryResultsModifiable()}
   * returns true, otherwise it will just return the supplied list.
   * This method should be called by repository extensions when returning list results
   * when not using {@link #asList(Stream)}.
   *
   * @param <T>  the type of elements in list.
   * @param list the input list.
   * @return the output list
   */
  @Nonnull
  public static <T> List<T> toResults( @Nonnull final List<T> list )
  {
    return Arez.areRepositoryResultsModifiable() ? list : Collections.unmodifiableList( list );
  }

  /**
   * Convert specified stream to a list, wrapping as an immutable list if required.
   * This method should be called by repository extensions when returning list results.
   *
   * @param <T>    the type of elements in stream.
   * @param stream the input stream.
   * @return the output list
   */
  @Nonnull
  public static <T> List<T> asList( @Nonnull final Stream<T> stream )
  {
    return toResults( stream.collect( Collectors.toList() ) );
  }
}
