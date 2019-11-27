package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class ProtectedAccessOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  @OnDepsChange
  protected void onRenderDepsChange()
  {
  }
}
