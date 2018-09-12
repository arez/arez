package com.example.observed;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;
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

  @ObserverRef
  abstract Observer getRenderObserver();
}
