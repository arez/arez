package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class ProtectedAccessTrackedModel
{
  @Observe( executor = Executor.EXTERNAL )
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  protected final void onRenderDepsChange()
  {
  }
}
