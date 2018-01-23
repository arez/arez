package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public class DeprecatedMemoizeModel
{
  @Deprecated
  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
