package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedMustNotHaveParametersModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged( int x )
  {
  }
}
