package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObservedModel1
{
  @Observe( executor = Executor.APPLICATION )
  @Deprecated
  void render()
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
