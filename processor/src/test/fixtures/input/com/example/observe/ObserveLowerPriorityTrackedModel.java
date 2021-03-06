package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class ObserveLowerPriorityTrackedModel
{
  @Observe( executor = Executor.EXTERNAL, observeLowerPriorityDependencies = true )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }
}
