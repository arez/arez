package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import arez.annotations.SuppressArezWarnings;

@ArezComponent
abstract class Suppressed2PublicAccessOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:PublicHookMethod" )
  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
