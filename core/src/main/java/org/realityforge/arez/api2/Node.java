package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Node
{
  @Nonnull
  private final ArezContext _context;
  @Nullable
  private final String _name;

  Node( @Nonnull final ArezContext context, @Nullable final String name )
  {
    _context = Objects.requireNonNull( context );
    _name = ArezConfig.enableNames() ? Objects.requireNonNull( name ) : null;
    Guards.invariant( () -> ArezConfig.enableNames() || null == name,
                      () -> String.format( "Node passed a name '%s' but ArezConfig.ENABLE_NAMES is false", name ) );
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }

  @Nonnull
  public final String getName()
  {
    Guards.invariant( () -> ArezConfig.enableNames(),
                      () -> "Node.getName() invoked when ArezConfig.ENABLE_NAMES is false" );
    assert null != _name;
    return _name;
  }

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
