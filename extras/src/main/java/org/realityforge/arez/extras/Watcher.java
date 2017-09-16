package org.realityforge.arez.extras;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.ArezContext;
import org.realityforge.arez.ComputedValue;
import org.realityforge.arez.Node;
import org.realityforge.arez.Observer;
import org.realityforge.arez.Procedure;
import org.realityforge.arez.SafeFunction;

/**
 * This class is used to wait until a condition is true, then run effect and remove watch.
 *
 * <p>The condition function is run in a read-only, tracking transaction and will be re-evaluated
 * any time any of the observed elements are updated. The effect procedure is run in either a
 * read-only or read-write, non-tracking transaction.</p>
 *
 * <p>This is a good example of how the primitives provided by Arez can be glued together
 * to create higher level reactive elements.</p>
 */
public final class Watcher
  extends Node
{
  /**
   * The Computed value representing condition.
   */
  @Nonnull
  private final ComputedValue<Boolean> _conditionValue;
  /**
   * The observer that is observing condition, waiting until it is true.
   */
  private final Observer _observer;
  /**
   * Has dispose() been called.
   */
  private boolean _disposed;

  /**
   * Create the watcher.
   *
   * @param context   the Arez system to watch.
   * @param name      the debug name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when th effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   */
  public Watcher( @Nonnull final ArezContext context,
                  @Nullable final String name,
                  final boolean mutation,
                  @Nonnull final SafeFunction<Boolean> condition,
                  @Nonnull final Procedure effect )
  {
    super( context, name );
    _conditionValue = context.createComputedValue( name, condition, Objects::equals );
    final Procedure procedure = () -> {
      if ( Boolean.TRUE == _conditionValue.get() )
      {
        context.procedure( name, mutation, effect );
        dispose();
      }
    };
    /*
     * Mutation needs to be true as dispose is considered a WRITE operation.
     */
    _observer = context.autorun( name, true, procedure, false );
    /*
     * Need to define autorun/Observer and have it assigned to variable before
     * it can be run. This is incase the dispose() method is invoked as part of
     * effect or the condition starts tru and calls dispose.
     * Dispose would get a NullPointerException as _observer would be null.
     */
    context.triggerScheduler();
  }

  /**
   * Cancel the watch if it has not been triggered and release all underlying resources.
   */
  @Override
  public void dispose()
  {
    if ( !_disposed )
    {
      _observer.dispose();
      _conditionValue.dispose();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }
}
