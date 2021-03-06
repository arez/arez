package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;

@ArezComponent
abstract class TrackedOnDepsChangeAcceptsObserverModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  void onRenderDepsChange( @Nonnull final Observer observer )
  {
  }
}
