package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observe;

@ArezComponent
public abstract class ArezOrNoneDependenciesModel
{
  @Observe( depType = DepType.AREZ_OR_NONE )
  void render()
  {
  }
}
