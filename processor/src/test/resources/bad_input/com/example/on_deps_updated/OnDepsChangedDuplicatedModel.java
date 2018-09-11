package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedDuplicatedModel
{
  @Observed
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
