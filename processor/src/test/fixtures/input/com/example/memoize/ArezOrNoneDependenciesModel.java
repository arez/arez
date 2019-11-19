package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.DepType;

@ArezComponent
abstract class ArezOrNoneDependenciesModel
{
  @Memoize( depType = DepType.AREZ_OR_NONE )
  public long getTime()
  {
    return 0;
  }
}
