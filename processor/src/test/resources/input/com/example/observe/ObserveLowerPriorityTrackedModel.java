package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class ObserveLowerPriorityTrackedModel
{
  @Observe( executor = Executor.APPLICATION, observeLowerPriorityDependencies = true )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
