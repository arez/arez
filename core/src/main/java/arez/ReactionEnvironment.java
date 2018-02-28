package arez;

import javax.annotation.Nonnull;

/**
 * Interface that used to setup environment in which to run reactions.
 * Typically this is used to flag other systems to enable change batching etc.
 */
@FunctionalInterface
public interface ReactionEnvironment
{
  /**
   * Run the specified action.
   * The interface will ensure that the environment is setup prior to invoking the action and reset after
   * invoking the action.
   *
   * @param action the action.
   */
  void run( @Nonnull SafeProcedure action );
}
