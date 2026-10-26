package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.ComputableValueRef;
import arez.annotations.Memoize;
import arez.annotations.DepType;
import arez.annotations.Feature;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultWriteOutsideTransaction = Feature.ENABLE )
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

  @Memoize( depType = DepType.AREZ_OR_EXTERNAL, writeOutsideTransaction = Feature.ENABLE )
  public long getEnabledTime()
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue<Long> getEnabledTimeComputableValue();

  @Memoize( depType = DepType.AREZ_OR_EXTERNAL, writeOutsideTransaction = Feature.DISABLE )
  public long getDisabledTime()
  {
    return 0;
  }

  @ComputableValueRef
  abstract ComputableValue<Long> getDisabledTimeComputableValue();
}
