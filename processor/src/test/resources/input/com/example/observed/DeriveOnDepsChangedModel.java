package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeriveOnDepsChangedModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render( final long time, float someOtherParameter )
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
