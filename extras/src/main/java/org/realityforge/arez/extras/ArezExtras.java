package org.realityforge.arez.extras;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.realityforge.arez.Arez;
import org.realityforge.arez.Node;
import org.realityforge.arez.Procedure;
import org.realityforge.arez.SafeFunction;
import org.realityforge.arez.Unsupported;

/**
 * Simplified interface for interacting with extras package.
 */
@Unsupported( "Expect this class to evolve over time" )
public final class ArezExtras
{
  /**
   * Id of next node to be created.
   * This is only used if {@link org.realityforge.arez.ArezContext#areNamesEnabled()} returns true but no name has been supplied.
   */
  private static int c_nextNodeId = 1;

  private ArezExtras()
  {
  }

  /**
   * Wait until a condition is true, then run effect. The effect is run in a read-only transaction.
   * See {@link #when(String, boolean, SafeFunction, Procedure)} for further details.
   *
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   */
  public static Node when( @Nonnull final SafeFunction<Boolean> condition,
                           @Nonnull final Procedure effect )
  {
    return when( false, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(String, boolean, SafeFunction, Procedure)} for further details.
   *
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   */
  public static Node when( final boolean mutation,
                           @Nonnull final SafeFunction<Boolean> condition,
                           @Nonnull final Procedure effect )
  {
    return when( null, mutation, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * The condition function is run in a read-only, tracking transaction and will be re-evaluated
   * any time any of the observed elements are updated. The effect procedure is run in either a
   * read-only or read-write, non-tracking transaction.</p>
   *
   * @param name      the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   */
  public static Node when( @Nullable final String name,
                           final boolean mutation,
                           @Nonnull final SafeFunction<Boolean> condition,
                           @Nonnull final Procedure effect )
  {
    return new Watcher( Arez.context(), toName( "When", name ), mutation, condition, effect );
  }

  /**
   * Build name for node.
   * If {@link org.realityforge.arez.ArezContext#areNamesEnabled()} returns false then this method will return null, otherwise the specified
   * name will be returned or a name synthesized from the prefix and a running number if no name is specified.
   *
   * @param prefix the prefix used if this method needs to generate name.
   * @param name   the name specified by the user.
   * @return the name.
   */
  @Nullable
  static String toName( @Nonnull final String prefix, @Nullable final String name )
  {
    return Arez.context().areNamesEnabled() ?
           null != name ? name : prefix + "@" + c_nextNodeId++ :
           null;
  }

  @TestOnly
  static void setNextNodeId( final int nextNodeId )
  {
    c_nextNodeId = nextNodeId;
  }

  @TestOnly
  static int getNextNodeId()
  {
    return c_nextNodeId;
  }
}
