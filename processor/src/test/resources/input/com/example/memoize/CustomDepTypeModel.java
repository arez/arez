package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.DepType;

@ArezComponent
public abstract class CustomDepTypeModel
{
  @Memoize( depType = DepType.AREZ_OR_NONE )
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
