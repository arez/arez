package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Node
  extends ArezElement
{
  @Nonnull
  private final ArezContext _context;
  /**
   * Uniquely identifies node within the system.
   * It is used by certain sub-classes (i.e. Transaction) to optimize state tracking.
   */
  private final int _id;

  Node( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( name );
    _context = Objects.requireNonNull( context );
    _id = context.nextNodeId();
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
}
