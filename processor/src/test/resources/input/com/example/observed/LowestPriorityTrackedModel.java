package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityTrackedModel
{
  @Observed( executor = Executor.APPLICATION, priority = Priority.LOWEST )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
