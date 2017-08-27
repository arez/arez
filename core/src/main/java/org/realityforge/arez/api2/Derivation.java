package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Derivation
  extends Observer
{
  Derivation( @Nonnull final ArezContext context, @Nullable final String name )
  {
    super( context, name );
  }

  @Override
  protected void onBecomeUnobserved()
  {
    getDependencies().forEach( dependency -> dependency.removeObserver( this ) );
    getDependencies().clear();
  }

  /**
   * Return true if derivation is generating new values.
   */
  final boolean isActive()
  {
    return ObserverState.NOT_TRACKING != getState();
  }
}
