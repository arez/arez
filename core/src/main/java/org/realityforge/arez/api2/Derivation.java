package org.realityforge.arez.api2;

import javax.annotation.Nonnull;

public class Derivation
  extends Node
{
  @Nonnull
  private ObserverState _dependenciesState = ObserverState.NOT_TRACKING;

  public Derivation( @Nonnull final ArezContext context,
                     @Nonnull final String name )
  {
    super( context, name );
  }
}
