package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observe;

@ArezComponent
public abstract class NonArezDependenciesButNoObserverRefModel
{
  @Observe( depType = DepType.AREZ_OR_EXTERNAL )
  void doStuff()
  {
  }
}
