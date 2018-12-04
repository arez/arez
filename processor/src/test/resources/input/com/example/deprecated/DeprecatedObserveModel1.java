package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObserveModel1
{
  @Observe( executor = Executor.EXTERNAL )
  @Deprecated
  void render()
  {
  }

  public void onRenderDepsChange()
  {
  }
}
