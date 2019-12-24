package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import javax.annotation.Nonnull;

@ArezComponent
abstract class TrackedImplicitOnDepsChangeAcceptsObserverModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  final void onRenderDepsChange( @Nonnull final Observer observer )
  {
  }
}
