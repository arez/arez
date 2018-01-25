package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class BasicMemoizeModel
{
  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
