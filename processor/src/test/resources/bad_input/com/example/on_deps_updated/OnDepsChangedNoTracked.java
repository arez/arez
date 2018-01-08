package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public class OnDepsChangedNoTracked
{
  @OnDepsChanged
  void onRenderDepsChanged()
  {
  }
}
