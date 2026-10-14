package com.example.deprecated;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class DeprecatedObserveModel4
{
  void render()
  {
  }

  @Deprecated
  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @ObserverRef
  abstract @Nullable Observer getRenderObserver();
}
