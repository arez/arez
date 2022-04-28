package arez;

import arez.spy.Spy;
import grim.annotations.OmitSymbol;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * A node within an Arez dependency graph.
 * The node is a named element within a specific Arez system that forms part of the
 * dependency graph.
 *
 * <p>The Node class can be extended by classes outside the Arez core package. Typically this is
 * done when a collection of primitive types (i.e. Observables, Observers, ComputableValues etc) are
 * aggregated to form a single abstraction within the reactive system.</p>
 */
public abstract class Node
  implements Disposable
{
  /**
   * Reference to the context to which this node belongs.
   */
  @OmitSymbol( unless = "arez.enable_zones" )
  @Nullable
  private final ArezContext _context;
  /**
   * A human consumable name for node. It should be non-null if {@link Arez#areNamesEnabled()} returns
   * true and <code>null</code> otherwise.
   */
  @Nullable
  @OmitSymbol( unless = "arez.enable_names" )
  private final String _name;

  Node( @Nullable final ArezContext context, @Nullable final String name )
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( () -> Arez.areZonesEnabled() || null == context,
                    () -> "Arez-0180: Node passed a context but Arez.areZonesEnabled() is false" );
      apiInvariant( () -> Arez.areNamesEnabled() || null == name,
                    () -> "Arez-0052: Node passed a name '" + name + "' but Arez.areNamesEnabled() is false" );
    }
    _context = Arez.areZonesEnabled() ? Objects.requireNonNull( context ) : null;
    _name = Arez.areNamesEnabled() ? Objects.requireNonNull( name ) : null;
  }

  /**
   * Return the name of the node.
   * This method should NOT be invoked unless {@link Arez#areNamesEnabled()} returns <code>true</code>.
   *
   * @return the name of the node.
   */
  @Nonnull
  public final String getName()
  {
    if ( Arez.shouldCheckApiInvariants() )
    {
      apiInvariant( Arez::areNamesEnabled,
                    () -> "Arez-0053: Node.getName() invoked when Arez.areNamesEnabled() is false" );
    }
    assert null != _name;
    return _name;
  }

  /**
   * Return the context that the node is associated with.
   *
   * @return the associated ArezContext.
   */
  @Nonnull
  public final ArezContext getContext()
  {
    return Arez.areZonesEnabled() ? Objects.requireNonNull( _context ) : Arez.context();
  }

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

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  final boolean willPropagateSpyEvents()
  {
    return Arez.areSpiesEnabled() && getSpy().willPropagateSpyEvents();
  }

  /**
   * Return the spy associated with context.
   * This method should not be invoked unless {@link Arez#areSpiesEnabled()} returns true.
   *
   * @return the spy associated with context.
   */
  @Nonnull
  final Spy getSpy()
  {
    return getContext().getSpy();
  }

  /**
   * Report a spy event.
   *
   * @param event the event that occurred.
   */
  final void reportSpyEvent( @Nonnull final Object event )
  {
    getSpy().reportSpyEvent( event );
  }
}
