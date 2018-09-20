package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Observed;

@ArezComponent
public abstract class ArezOrNoneDependenciesModel
{
  @Observed( depType = DepType.AREZ_OR_NONE )
  void render()
  {
  }
}
