package com.example.deprecated;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class DeprecatedObserveModel3
{
  @Deprecated
  void render()
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}
