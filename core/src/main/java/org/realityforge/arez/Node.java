package org.realityforge.arez;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A node within an Arez dependency graph.
 * The node is a named element within a specific Arez system that forms part of the dependency graph.
 */
public abstract class Node
{
  /**
   * Reference to the system to which this node belongs.
   */
  @Nonnull
  private final ArezContext _context;
  /**
   * A human consumable name for node. It should be non-null if {@link ArezConfig#enableNames()} returns
   * true and <tt>null</tt> otherwise.
   */
  @Nullable
  private final String _name;
  /**
   * Uniquely identifies node within the system.
   * It is used by certain sub-classes (i.e. Transaction) to optimize state tracking.
   */
  private final int _id;

  Node( @Nonnull final ArezContext context, @Nullable final String name )
  {
    Guards.invariant( () -> ArezConfig.enableNames() || null == name,
                      () -> String.format( "Node passed a name '%s' but ArezConfig.enableNames() is false", name ) );
    _context = Objects.requireNonNull( context );
    _name = ArezConfig.enableNames() ? Objects.requireNonNull( name ) : null;
    _id = context.nextNodeId();
  }

  /**
   * Return the name of the node.
   * This method should NOT be invoked unless {@link ArezConfig#enableNames()} returns true and will throw an
   * exception if invariant checking is enabled.
   *
   * @return the name of the node.
   */
  @Nonnull
  public final String getName()
  {
    Guards.invariant( ArezConfig::enableNames,
                      () -> "ArezElement.getName() invoked when ArezConfig.enableNames() is false" );
    assert null != _name;
    return _name;
  }

  final int getId()
  {
    return _id;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public final String toString()
  {
    if ( ArezConfig.enableNames() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }

}
