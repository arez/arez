package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Node
  extends ArezElement
{
  @Nonnull
  private final ArezContext _context;

  Node( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( name );
    _context = Objects.requireNonNull( context );
  }

  @Nonnull
  final ArezContext getContext()
  {
    return _context;
  }
}
