package org.realityforge.arez;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface OnEntityRemovedCallback
{
  void onEntityRemoved( @Nonnull Object entity );
}
