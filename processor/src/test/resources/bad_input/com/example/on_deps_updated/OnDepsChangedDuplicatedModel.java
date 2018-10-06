package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedDuplicatedModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @OnDepsChanged( name = "render" )
  public void onRenderDepsChanged2()
  {
  }
}
