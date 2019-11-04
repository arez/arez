package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class TrackedNoOtherSchedulableModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render1()
  {
  }

  @OnDepsChange
  public void onRender1DepsChange()
  {
  }
}
