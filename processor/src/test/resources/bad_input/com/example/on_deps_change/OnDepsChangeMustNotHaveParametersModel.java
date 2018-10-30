package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeMustNotHaveParametersModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChange
  void onRenderDepsChange( int x )
  {
  }
}
