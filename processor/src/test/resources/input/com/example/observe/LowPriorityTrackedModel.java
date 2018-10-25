package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityTrackedModel
{
  @Observe( executor = Executor.APPLICATION, priority = Priority.LOW )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
