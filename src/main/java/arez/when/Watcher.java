package arez.when;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.ComputedValue;
import arez.Disposable;
import arez.Flags;
import arez.Observer;
import arez.SafeFunction;
import arez.SafeProcedure;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * This class is used to wait until a condition is true, then run effect and remove watch.
 *
 * <p>The condition function is run in a read-only, tracking transaction and will be re-evaluated
 * any time any of the observed elements are updated. The effect procedure is run in either a
 * read-only or read-write, non-tracking transaction.</p>
 */
final class Watcher
  implements Disposable
{
  /**
   * Reference to the system to which this node belongs.
   */
  @Nullable
  private final ArezContext _context;
  /**
   * A human consumable name for node. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * True if the effect should run in a read-write transaction.
   */
  private final boolean _mutation;
  /**
   * True if the effect should verify action is required.
   */
  private final boolean _verifyActionRequired;
  /**
   * The condition to test.
   */
  @Nonnull
  private final ComputedValue<Boolean> _condition;
  /**
   * The effect/action to run when condition is true.
   */
  private final SafeProcedure _effect;
  /**
   * The observer responsible for watching the condition and running the effect reaction when condition triggered.
   */
  private final Observer _observer;

  /**
   * Create the watcher.
   *
   * @param context        the Arez context.
   * @param name           the name (if any) used when naming the underlying Arez resources.
   * @param mutation       true if the effect can mutate state, false otherwise.
   * @param condition      The function that determines when the effect is run.
   * @param effect         The procedure that is executed when the condition is true.
   * @param runImmediately True if condition should be scheduled immediately.
   */
  Watcher( @Nullable final ArezContext context,
           @Nullable final Component component,
           @Nullable final String name,
           final boolean mutation,
           final boolean verifyActionRequired,
           @Nonnull final SafeFunction<Boolean> condition,
           @Nonnull final SafeProcedure effect,
           final int priority,
           final boolean runImmediately )
  {
    if ( Arez.shouldCheckInvariants() )
    {
      apiInvariant( () -> Arez.areZonesEnabled() || null == context,
                    () -> "Watcher passed a context but Arez.areZonesEnabled() is false" );
    }
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                    () -> "Watcher passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
      apiInvariant( () -> Flags.PRIORITY_HIGHEST == priority ||
                          Flags.PRIORITY_HIGH == priority ||
                          Flags.PRIORITY_NORMAL == priority ||
                          Flags.PRIORITY_LOW == priority ||
                          Flags.PRIORITY_LOWEST == priority,
                    () -> "Watcher named '" + name + "' passed an invalid priority: " + priority );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
    _mutation = mutation;
    _verifyActionRequired = verifyActionRequired;
    _effect = Objects.requireNonNull( effect );
    _condition =
      getContext().computed( Arez.areNativeComponentsEnabled() ? component : null,
                             Arez.areNamesEnabled() ? getName() + ".condition" : null,
                             Objects.requireNonNull( condition ),
                             null,
                             this::dispose,
                             null,
                             priority );
    _observer =
      getContext().observer( Arez.areNativeComponentsEnabled() ? component : null,
                             Arez.areNamesEnabled() ? getName() + ".watcher" : null,
                             this::checkCondition,
                             priority | Flags.READ_WRITE | Flags.RUN_LATER | Flags.NESTED_ACTIONS_ALLOWED );
    /*
     * Can not pass this as flag when constructing watcher, otherwise this class could attempt
     * to dispose when condition starts as being true before _observer has been assigned which
     * results in either a dangling life watcher or an invariant failure depending on configuration
     * of the application.
     */
    if ( runImmediately )
    {
      getContext().triggerScheduler();
    }
  }

  /**
   * Return the name of the node.
   * This method should NOT be invoked unless {@link Arez#areNamesEnabled()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the node.
   */
  @Nonnull
  public final String getName()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNamesEnabled, () -> "Watcher.getName() invoked when Arez.areNamesEnabled() is false" );
    }
    assert null != _name;
    return _name;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String toString()
  {
    if ( Arez.areNamesEnabled() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }

  @Nonnull
  Observer getObserver()
  {
    return _observer;
  }

  /**
   * Check the condition and when it returns true the run the effect and dispose the watcher.
   */
  private void checkCondition()
  {
    if ( Disposable.isNotDisposed( _condition ) && _condition.get() )
    {
      getContext().safeAction( Arez.areNamesEnabled() ? getName() : null, _mutation, _verifyActionRequired, _effect );
      Disposable.dispose( _observer );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose()
  {
    Disposable.dispose( _observer );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDisposed()
  {
    return Disposable.isDisposed( _observer );
  }

  boolean isMutation()
  {
    return _mutation;
  }

  @Nonnull
  ComputedValue<Boolean> getCondition()
  {
    return _condition;
  }

  SafeProcedure getEffect()
  {
    return _effect;
  }
}
