package org.realityforge.arez;

import javax.annotation.Nonnull;

public interface IReactionErrorHandler
{
  void onError( @Nonnull Throwable error, @Nonnull IDerivation derivation );
}
