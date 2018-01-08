package arez.extras;

import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.SafeFunction;
import arez.SafeProcedure;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Computed;
import arez.annotations.ContextRef;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.TestOnly;

/**
 * This class is used to wait until a condition is true, then run effect and remove watch.
 *
 * <p>The condition function is run in a read-only, tracking transaction and will be re-evaluated
 * any time any of the observed elements are updated. The effect procedure is run in either a
 * read-only or read-write, non-tracking transaction.</p>
 */
@ArezComponent
class Watcher
{
  /**
   * A human consumable name for effect. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * True if the effect should run in a read-write transaction.
   */
  private final boolean _mutation;
  /**
   * The condition to test.
   */
  @Nonnull
  private final SafeFunction<Boolean> _condition;
  /**
   * The effect/action to run when condition is true.
   */
  private final SafeProcedure _effect;

  /**
   * Create the watcher.
   *
   * @param name      the name (if any) used when naming the underlying Arez resources.
   * @param mutation  true if the effect can mutate state, false otherwise.
   * @param condition The function that determines when the effect is run.
   * @param effect    The procedure that is executed when the condition is true.
   */
  Watcher( @Nullable final String name,
           final boolean mutation,
           @Nonnull final SafeFunction<Boolean> condition,
           @Nonnull final SafeProcedure effect )
  {
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _mutation = mutation;
    _condition = Objects.requireNonNull( condition );
    _effect = Objects.requireNonNull( effect );
  }

  /**
   * Get access to the context associated with the watcher.
   */
  @ContextRef
  ArezContext context()
  {
    throw new IllegalStateException();
  }

  /**
   * Check the condition and when it returns true the run the effec and dispose the watcher.
   */
  @Autorun
  void checkCondition()
  {
    if ( condition() )
    {
      context().safeAction( _name, _mutation, _effect );
      Disposable.dispose( this );
    }
  }

  @Computed
  boolean condition()
  {
    return _condition.call();
  }

  @TestOnly
  @Nullable
  String getName()
  {
    return _name;
  }

  @TestOnly
  boolean isMutation()
  {
    return _mutation;
  }

  @TestOnly
  @Nonnull
  SafeFunction<Boolean> getCondition()
  {
    return _condition;
  }

  @TestOnly
  SafeProcedure getEffect()
  {
    return _effect;
  }
}
