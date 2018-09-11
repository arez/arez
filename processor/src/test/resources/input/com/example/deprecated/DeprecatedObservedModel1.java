package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedObservedModel1
{
  @Observed
  @Deprecated
  void render()
  {
  }

  public void onRenderDepsChanged()
  {
  }
}
