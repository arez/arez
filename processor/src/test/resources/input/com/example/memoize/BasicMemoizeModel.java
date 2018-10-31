package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class BasicMemoizeModel
{
  @Computed
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
