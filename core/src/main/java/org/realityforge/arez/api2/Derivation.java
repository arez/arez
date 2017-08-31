package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Derivation
  extends Reaction
{
  Derivation( @Nonnull final ArezContext context, @Nullable final String name, @Nonnull final TransactionMode mode )
  {
    super( context, name, mode );
  }

  /**
   * Passivate the derivation.
   * The derivation will no longer generate new observable values and dependencies are released.
   */
  protected void passivate()
  {
    Guards.invariant( this::isActive,
                      () -> String.format( "Invoked passivate on derivation named '%s' when derivation is not active.",
                                           getName() ) );
    setState( ObserverState.INACTIVE );
  }
}
