package com.example.observed;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;

@ArezComponent
public abstract class NonArezDependenciesModel
{
  @Observed( depType = DepType.AREZ_OR_EXTERNAL )
  void render()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}
