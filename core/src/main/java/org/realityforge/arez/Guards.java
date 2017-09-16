package org.realityforge.arez;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.Contract;

/**
 * A utility class used to perform assertions and invariant checks.
 */
final class Guards
{
  private Guards()
  {
  }

  /**
   * Check an invariant in code base.
   * If the condition is false then an {@link IllegalStateException} is thrown.
   * The invariant check will be skipped unless the configuration setting {@link ArezConfig#checkInvariants()}
   * is true. A null message is used rather than supplied message unless {@link ArezConfig#verboseErrorMessages()}
   * is true.
   *
   * @param condition the condition to check.
   * @param message   the message supplier used if verbose messages enabled.
   * @throws IllegalStateException if condition returns false.
   */
  static void invariant( @Nonnull final Supplier<Boolean> condition,
                         @Nonnull final Supplier<String> message )
  {
    if ( ArezConfig.checkInvariants() )
    {
      boolean conditionResult = false;
      try
      {
        conditionResult = condition.get();
      }
      catch ( final Throwable t )
      {
        fail( () -> "Error checking condition.\n" +
                    "Message: " + ArezUtil.safeGetString( message ) + "\n" +
                    "Throwable:\n" +
                    ThrowableUtil.throwableToString( t ) );
      }
      if ( !conditionResult )
      {
        fail( message );
      }
    }
  }

  /**
   * Throw an IllegalStateException with supplied detail message.
   * The exception is not thrown unless {@link ArezConfig#checkInvariants()} is true.
   * The exception will ignore the supplied message unless {@link ArezConfig#verboseErrorMessages()} is true.
   *
   * @param message the message supplier used if verbose messages enabled.
   * @throws IllegalStateException when called.
   */
  @Contract( "_ -> fail" )
  static void fail( @Nonnull final Supplier<String> message )
  {
    if ( ArezConfig.checkInvariants() )
    {
      if ( ArezConfig.verboseErrorMessages() )
      {
        throw new IllegalStateException( ArezUtil.safeGetString( message ) );
      }
      else
      {
        throw new IllegalStateException();
      }
    }
  }
}
