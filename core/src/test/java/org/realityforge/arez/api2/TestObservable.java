package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class TestObservable
  extends Observable
{
  TestObservable( @Nonnull final ArezContext context,
                  @Nullable final String name )
  {
    super( context, name );
  }

  TestObservable( @Nonnull final ArezContext context,
                  @Nullable final String name,
                  @Nullable final Derivation derivation )
  {
    super( context, name, derivation );
  }
}
