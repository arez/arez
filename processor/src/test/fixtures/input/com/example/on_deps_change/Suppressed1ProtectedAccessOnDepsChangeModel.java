package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class Suppressed1ProtectedAccessOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:ProtectedMethod" )
  @OnDepsChange
  protected void onRenderDepsChange()
  {
  }
}
