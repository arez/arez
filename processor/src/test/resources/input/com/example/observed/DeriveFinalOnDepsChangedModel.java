package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeriveFinalOnDepsChangedModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render( final long time, float someOtherParameter )
  {
  }

  final void onRenderDepsChanged()
  {
  }
}
