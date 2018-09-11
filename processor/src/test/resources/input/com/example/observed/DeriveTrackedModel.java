package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class DeriveTrackedModel
{
  void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
