package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.DepType;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class CustomDepTypeModel
{
  @Memoize( depType = DepType.AREZ_OR_NONE )
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
