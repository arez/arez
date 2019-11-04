package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeNotStaticModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChange
  static void onRenderDepsChange()
  {
  }
}
