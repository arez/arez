package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class TrackedAndSchedulableModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render1()
  {
  }

  @Observe
  void render2()
  {
  }

  @OnDepsChange
  public void onRender1DepsChange()
  {
  }
}
