package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedNotStaticModel
{
  @Observed
  void render()
  {
  }

  @OnDepsChanged
  static void onRenderDepsChanged()
  {
  }
}
