package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedObservedModel1
{
  @Observed( executor = Executor.APPLICATION )
  @Deprecated
  void render()
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
