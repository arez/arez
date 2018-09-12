package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class ProtectedAccessTrackedModel
{
  @Observed( executor = Executor.APPLICATION )
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected final void onRenderDepsChanged()
  {
  }
}
