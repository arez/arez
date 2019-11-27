package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.annotations.Priority;

@ArezComponent
abstract class HighPriorityTrackedModel
{
  @Observe( executor = Executor.EXTERNAL, priority = Priority.HIGH )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }
}
