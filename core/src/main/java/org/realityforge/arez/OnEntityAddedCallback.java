package org.realityforge.arez;

import javax.annotation.Nonnull;

/**
 * This callback is invoked when the entity is created and/or attatched to the observable subsystem.
 */
@FunctionalInterface
public interface OnEntityAddedCallback
{
  void onEntityAdded( @Nonnull Object entity );
}
