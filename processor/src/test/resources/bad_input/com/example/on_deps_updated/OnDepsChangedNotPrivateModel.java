package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedNotPrivateModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChanged
  private void onRenderDepsChanged()
  {
  }
}
