package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;

public abstract class Node
{
  @Nonnull
  private final ArezContext _context;
  @Nonnull
  private final String _name;

  protected Node( @Nonnull final ArezContext context, @Nonnull final String name )
  {
    _context = Objects.requireNonNull( context );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  protected final ArezContext getContext()
  {
    return _context;
  }

  @Nonnull
  public final String getName()
  {
    return _name;
  }

  @Nonnull
  @Override
  public final String toString()
  {
    return getName();
  }
}
