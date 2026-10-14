package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnDepsChangeNotAbstractModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChange
  abstract void onRenderDepsChange();
}
