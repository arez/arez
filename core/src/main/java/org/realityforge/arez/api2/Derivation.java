package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Derivation
  extends Reaction
{
  Derivation( @Nonnull final ArezContext context,
              @Nullable final String name,
              @Nonnull final TransactionMode mode,
              @Nonnull final Action action )
  {
    super( context, name, mode, action );
  }
}
