package org.realityforge.arez.api2;

import javax.annotation.Nonnull;

public interface Observer
{
  @Nonnull
  String getName();

  @Nonnull
  ObserverState getState();

  void setState( @Nonnull ObserverState state );
}
