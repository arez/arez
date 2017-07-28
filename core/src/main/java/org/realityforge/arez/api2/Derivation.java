package org.realityforge.arez.api2;

import javax.annotation.Nonnull;

public class Derivation
  extends Node
{
  /**
   * The stalest state of the associated observables that are also derivations.
   */
  @Nonnull
  private ObserverState _dependenciesState = ObserverState.NOT_TRACKING;

  public Derivation( @Nonnull final ArezContext context,
                     @Nonnull final String name )
  {
    super( context, name );
  }

  @Override
  public void setState( @Nonnull final ObserverState state )
  {
    _dependenciesState = state;
    if ( state == ObserverState.STALE || state == ObserverState.POSSIBLY_STALE )
    {
      onBecomeStale();
    }
  }
}
