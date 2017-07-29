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
    _name = ArezConfig.ENABLE_NAMES ? Objects.requireNonNull( name ) : null;
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }

  @Nonnull
  public final String getName()
  {
    Guards.invariant( () -> ArezConfig.ENABLE_NAMES,
                      () -> "Node.getName() invoked when ArezConfig.ENABLE_NAMES is false" );
    assert null != _name;
    return _name;
  }

  @Nonnull
  @Override
  public final String toString()
  {
    if ( ArezConfig.ENABLE_NAMES )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }
}
