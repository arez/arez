package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObserveModel2
{
  @Observe( executor = Executor.EXTERNAL )
  void render()
  {
  }

  @Deprecated
  public void onRenderDepsChange()
  {
  }
}
