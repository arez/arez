package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeriveFinalOnDepsChangedModel
{
  @Observe( executor = Executor.APPLICATION )
  public void render( final long time, float someOtherParameter )
  {
  }

  final void onRenderDepsChanged()
  {
  }
}
