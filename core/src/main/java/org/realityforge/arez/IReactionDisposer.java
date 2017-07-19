package org.realityforge.arez;

import javax.annotation.Nonnull;

public interface IReactionDisposer
{
  void run();

  @Nonnull
  Reaction getImplementation();

  void setOnError( @Nonnull IReactionErrorHandler handler );
}
