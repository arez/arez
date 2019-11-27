package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
abstract class DeprecatedObserveModel2
{
  @Observe( executor = Executor.EXTERNAL )
  void render()
  {
  }

  @Deprecated
  void onRenderDepsChange()
  {
  }
}
