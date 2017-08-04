package org.realityforge.arez.api2;

import javax.annotation.Nonnull;

public class ObservableValue
  extends Observable
{
  public ObservableValue( @Nonnull final ArezContext context, @Nonnull final Derivation derivation )
  {
    super( context, "ObservableValue@" + context.nextNodeId(), derivation );
  }
}
