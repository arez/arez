package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeDuplicatedModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }

  @OnDepsChange( name = "render" )
  public void onRenderDepsChange2()
  {
  }
}
