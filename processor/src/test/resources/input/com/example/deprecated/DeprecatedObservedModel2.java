package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedObservedModel2
{
  @Observed( executor = Executor.APPLICATION )
  void render()
  {
  }

  @Deprecated
  public void onRenderDepsChanged()
  {
  }
}
