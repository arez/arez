package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import arez.annotations.DepType;
import javax.annotation.Nonnull;

@ArezComponent
abstract class NonArezDependenciesModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public long getTime()
  {
    return 0;
  }

  @Nonnull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
