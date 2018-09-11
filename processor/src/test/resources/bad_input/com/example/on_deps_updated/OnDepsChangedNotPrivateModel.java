package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedNotPrivateModel
{
  @Observed
  void render()
  {
  }

  @OnDepsChanged
  private void onRenderDepsChanged()
  {
  }
}
