package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2ProtectedAccessOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @OnDepsChange
  protected void onRenderDepsChange()
  {
  }
}
