package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeThrowsExceptionModel
{
  @Observe
  void render()
  {
  }

  @SuppressWarnings( "RedundantThrows" )
  @OnDepsChange
  void onRenderDepsChange()
    throws Exception
  {
  }
}
