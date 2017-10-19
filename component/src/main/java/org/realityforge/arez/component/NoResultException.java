package org.realityforge.arez.component;

import javax.annotation.Nullable;

/**
 * Exception thrown by repository when query produces no result when a result was expected.
 */
public class NoResultException
  extends RuntimeException
{
  /**
   * Create the exception.
   */
  public NoResultException()
  {
  }

  /**
   * Create the exception.
   *
   * @param message the detail message of exception.
   */
  public NoResultException( @Nullable final String message )
  {
    super( message );
  }

  /**
   * Create the exception.
   *
   * @param message the detail message of exception.
   * @param cause   the ultimate cause of the exception.
   */
  public NoResultException( @Nullable final String message, @Nullable final Throwable cause )
  {
    super( message, cause );
  }
}
