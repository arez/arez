package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class OnDepsChangeNoTracked
{
  @OnDepsChange
  void onRenderDepsChange()
  {
  }
}
