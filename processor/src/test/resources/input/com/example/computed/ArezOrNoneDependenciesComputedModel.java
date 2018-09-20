package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;

@ArezComponent
public abstract class ArezOrNoneDependenciesComputedModel
{
  @Computed( depType = DepType.AREZ_OR_NONE )
  public long getTime()
  {
    return 0;
  }
}
