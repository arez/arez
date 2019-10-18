package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import javax.annotation.Nonnull;

@ArezComponent
abstract class OnActivateWithComputableValueParamModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  final void onTimeActivate( @Nonnull final ComputableValue<Long> computableValue )
  {
  }
}
