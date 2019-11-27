package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class Suppressed1PublicAccessOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicHookMethod" )
  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
