package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Derivation
  extends Observer
{
  Derivation( @Nonnull final ArezContext context,
              @Nullable final String name,
              @Nonnull final TransactionMode mode,
              @Nonnull final Reaction reaction )
  {
    super( context, name, mode, reaction );
  }

  /**
   * Ensure that state field and other fields of the Derivation are consistent.
   */
  final void invariantDerivationState()
  {
    invariantState();
    if ( isActive() )
    {
      Guards.invariant( () -> !getDependencies().isEmpty(),
                        () -> String.format( "Derivation named '%s' is active but has no dependencies.", getName() ) );
    }
  }
}
