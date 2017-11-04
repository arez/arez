package org.realityforge.arez.extras;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.Arez;
import org.realityforge.arez.Disposable;
import org.realityforge.arez.SafeFunction;
import org.realityforge.arez.SafeProcedure;

/**
 * Simplified interface for interacting with extras package.
 */
@Unsupported( "Expect this class to evolve over time" )
public final class ArezExtras
{
  private ArezExtras()
  {
  }

  /**
   * Wait until a condition is true, then run effect. The effect is run in a read-only transaction.
   * See {@link #when(String, boolean, SafeFunction, SafeProcedure)} for further details.
   *
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Disposable when( @Nonnull final SafeFunction<Boolean> condition,
                                 @Nonnull final SafeProcedure effect )
  {
    return when( false, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(String, boolean, SafeFunction, SafeProcedure)} for further details.
   *
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Disposable when( final boolean mutation,
                                 @Nonnull final SafeFunction<Boolean> condition,
                                 @Nonnull final SafeProcedure effect )
  {
    return when( null, mutation, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * The condition function is run in a read-only, tracking transaction and will be re-evaluated
   * any time any of the observed elements are updated. The effect procedure is run in either a
   * read-only or read-write, non-tracking transaction.
   *
   * @param name      the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Node representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Disposable when( @Nullable final String name,
                                 final boolean mutation,
                                 @Nonnull final SafeFunction<Boolean> condition,
                                 @Nonnull final SafeProcedure effect )
  {
    return new Arez_Watcher( Arez.context().generateNodeName( "When", name ), mutation, condition, effect );
  }
}
