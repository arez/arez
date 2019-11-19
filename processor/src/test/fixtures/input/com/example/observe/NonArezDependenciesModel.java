package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;

@ArezComponent
abstract class NonArezDependenciesModel
{
  @Observe( depType = DepType.AREZ_OR_EXTERNAL )
  void render()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}
