package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import arez.annotations.DepType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonArezDependenciesModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public long getTime()
  {
    return 0;
  }

  @NonNull
  @ComputableValueRef
  abstract ComputableValue<Long> getTimeComputableValue();
}
