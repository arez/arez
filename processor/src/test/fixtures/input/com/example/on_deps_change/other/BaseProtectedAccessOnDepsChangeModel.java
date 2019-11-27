package com.example.on_deps_change.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

public abstract class BaseProtectedAccessOnDepsChangeModel
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
