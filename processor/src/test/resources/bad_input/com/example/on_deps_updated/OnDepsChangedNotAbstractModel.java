package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedNotAbstractModel
{
  @Observed
  void render()
  {
  }

  @OnDepsChanged
  abstract void onRenderDepsChanged();
}
