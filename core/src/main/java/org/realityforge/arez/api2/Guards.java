package org.realityforge.arez.api2;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

final class Guards
{
  private Guards()
  {
  }

  /**
   * Check an invariant in code base.
   * If the condition is false then an {@link IllegalStateException} is thrown.
   * The invariant check will be skipped unless the configuration setting {@link ArezConfig#CHECK_INVARIANTS}
   * is true. A null message is used rather than supplied message unless {@link ArezConfig#VERBOSE_ERROR_MESSAGES}
   * is true.
   *
   * @throws IllegalStateException if condition returns false.
   */
  static void invariant( @Nonnull final Supplier<Boolean> condition,
                         @Nonnull final Supplier<String> message )
  {
    if ( ArezConfig.checkInvariants() && !condition.get() )
    {
      fail( message );
    }
  }

  /**
   * Throw an IllegalStateException with supplied detail message.
   * The exception is not thrown unless {@link ArezConfig#CHECK_INVARIANTS} is true.
   * The exception will ignore the supplied message unless {@link ArezConfig#VERBOSE_ERROR_MESSAGES} is true.
   */
  static void fail( @Nonnull final Supplier<String> message )
  {
    if ( ArezConfig.checkInvariants() )
    {
      throw new IllegalStateException( ArezConfig.verboseErrorMessages() ? message.get() : null );
    }
  }
}
