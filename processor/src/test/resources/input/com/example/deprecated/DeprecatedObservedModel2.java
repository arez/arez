package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class DeprecatedObservedModel2
{
  @Observe( executor = Executor.APPLICATION )
  void render()
  {
  }

  @Deprecated
  public void onRenderDepsChanged()
  {
  }
}
