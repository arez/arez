package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.DepType;

@ArezComponent
public abstract class CustomDepTypeMemoizeModel
{
  @Computed( depType = DepType.AREZ_OR_NONE )
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
