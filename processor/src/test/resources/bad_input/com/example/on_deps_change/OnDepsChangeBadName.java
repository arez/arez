package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeBadName
{
  @Observe
  void render()
  {
  }

  @OnDepsChange( name = "-ace" )
  void onRenderDepsChange()
  {
  }
}
