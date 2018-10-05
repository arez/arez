package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedMustNotReturnValueModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChanged
  int onRenderDepsChanged()
  {
    return 0;
  }
}
