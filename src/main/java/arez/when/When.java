package arez.when;

import arez.Arez;
import arez.Component;
import arez.Observer;
import arez.Priority;
import arez.SafeFunction;
import arez.SafeProcedure;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The utility class providing watcher methods.
 */
public final class When
{
  /**
   * Id of next watcher to be created.
   * This is only used if {@link Arez#areNamesEnabled()} returns true but no name has been supplied.
   */
  private static int c_nextWatcherId = 1;

  private When()
  {
  }

  /**
   * Wait until a condition is true, then run effect. The effect is run in a read-only transaction.
   * See {@link #when(String, boolean, SafeFunction, SafeProcedure)} for further details.
   *
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nonnull final SafeFunction<Boolean> condition,
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
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( final boolean mutation,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect )
  {
    return when( null, mutation, condition, effect );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, Priority, boolean)} for further details.
   *
   * @param name      the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nullable final String name,
                               final boolean mutation,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect )
  {
    return when( name, mutation, condition, effect, true );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, Priority, boolean)} for further details.
   *
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nullable final String name,
                               final boolean mutation,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect,
                               final boolean runImmediately )
  {
    return when( name, mutation, condition, effect, Priority.NORMAL, runImmediately );
  }

  /**
   * Wait until a condition is true, then run effect.
   * See {@link #when(Component, String, boolean, SafeFunction, SafeProcedure, Priority, boolean)} for further details.
   *
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param priority       the priority of the observer.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nullable final String name,
                               final boolean mutation,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect,
                               @Nonnull final Priority priority,
                               final boolean runImmediately )
  {
    return when( null, name, mutation, condition, effect, priority, runImmediately );
  }

  /**
   * Wait until a condition is true, then run effect.
   * The condition function is run in a read-only, tracking transaction and will be re-evaluated
   * any time any of the observed elements are updated. The effect procedure is run in either a
   * read-only or read-write, non-tracking transaction.
   *
   * @param component      the component containing when observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name           the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param priority       the priority of the observer.
   * @param runImmediately true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nullable final Component component,
                               @Nullable final String name,
                               final boolean mutation,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect,
                               @Nonnull final Priority priority,
                               final boolean runImmediately )
  {
    return when( component, name, mutation, true, condition, effect, priority, runImmediately );
  }

  /**
   * Wait until a condition is true, then run effect.
   * The condition function is run in a read-only, tracking transaction and will be re-evaluated
   * any time any of the observed elements are updated. The effect procedure is run in either a
   * read-only or read-write, non-tracking transaction.
   *
   * @param component            the component containing when observer if any. Should be null if {@link Arez#areNativeComponentsEnabled()} returns false.
   * @param name                 the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation             true if the effect can mutate state, false otherwise.
   * @param verifyActionRequired true if the effect will add invariant checks to ensure reads or writes occur within
   *                             the scope of the effect.
   * @param condition            The function that determines when the effect is run.
   * @param effect               The procedure that is executed when the condition is true.
   * @param priority             the priority of the observer.
   * @param runImmediately       true to invoke condition immediately, false to schedule reaction for next reaction cycle.
   * @return the Observer representing the reactive component. The user can dispose the node if it is no longer required.
   */
  public static Observer when( @Nullable final Component component,
                               @Nullable final String name,
                               final boolean mutation,
                               final boolean verifyActionRequired,
                               @Nonnull final SafeFunction<Boolean> condition,
                               @Nonnull final SafeProcedure effect,
                               @Nonnull final Priority priority,
                               final boolean runImmediately )
  {
    return new Watcher( Arez.areZonesEnabled() ? Arez.context() : null,
                        component,
                        generateNodeName( name ),
                        mutation,
                        verifyActionRequired,
                        condition,
                        effect,
                        priority,
                        runImmediately ).getWatcher();
  }

  /**
   * Build name for node.
   * If {@link Arez#areNamesEnabled()} returns false then this method will return null, otherwise the specified
   * name will be returned or a name synthesized from the running number if no name is specified.
   *
   * @param name the name specified by the user.
   * @return the name.
   */
  @Nullable
  private static String generateNodeName( @Nullable final String name )
  {
    return Arez.areNamesEnabled() ?
           null != name ? name : "When@" + c_nextWatcherId++ :
           null;
  }
}
