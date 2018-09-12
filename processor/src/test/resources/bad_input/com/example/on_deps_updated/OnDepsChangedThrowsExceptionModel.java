package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedThrowsExceptionModel
{
  @Observed
  void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged()
    throws Exception
  {
  }
}
