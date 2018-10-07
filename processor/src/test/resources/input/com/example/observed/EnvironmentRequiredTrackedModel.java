package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class EnvironmentRequiredTrackedModel
{
  @Observe( executor = Executor.APPLICATION, requireEnvironment = true )
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
