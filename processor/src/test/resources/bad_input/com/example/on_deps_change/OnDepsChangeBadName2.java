package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeBadName2
{
  @Observe
  void render()
  {
  }

  @OnDepsChange( name = "class" )
  void onRenderDepsChange()
  {
  }
}
