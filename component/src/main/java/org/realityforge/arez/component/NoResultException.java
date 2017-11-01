package org.realityforge.arez.component;

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
}
