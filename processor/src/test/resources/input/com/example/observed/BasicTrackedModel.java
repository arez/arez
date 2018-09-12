package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class BasicTrackedModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
