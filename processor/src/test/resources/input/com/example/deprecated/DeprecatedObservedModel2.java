package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class DeprecatedObservedModel2
{
  @Observed
  void render()
  {
  }

  @Deprecated
  public void onRenderDepsChanged()
  {
  }
}
