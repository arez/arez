package com.example.deprecated;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class DeprecatedObservedModel4
{
  void render()
  {
  }

  @Deprecated
  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}