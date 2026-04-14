package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.DepType;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class NonArezDependenciesWithOnActivateParamModel
{
  @Memoize( depType = DepType.AREZ_OR_EXTERNAL )
  public long getTime()
  {
    return 0;
  }

  @OnActivate
  void onTimeActivate( final ComputableValue<Long> computableValue )
  {
  }
}
