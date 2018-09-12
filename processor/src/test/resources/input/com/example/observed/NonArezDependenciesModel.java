package com.example.observed;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class NonArezDependenciesModel
{
  @Observed( arezOnlyDependencies = false )
  void render()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}
