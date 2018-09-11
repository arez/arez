package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class DeprecatedObservedModel3
{
  @Deprecated
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
