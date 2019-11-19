package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class DeprecatedMemoize5Model
{
  @Deprecated
  @Memoize
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
