package org.realityforge.arez.api2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class TestObservable
  extends Observable
{
  public TestObservable( @Nonnull final ArezContext context,
                         @Nullable final String name )
  {
    super( context, name );
  }

  public TestObservable( @Nonnull final ArezContext context,
                         @Nullable final String name,
                         @Nullable final Observer observer )
  {
    super( context, name, observer );
  }
}
