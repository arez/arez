package com.example.on_activate;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadTypeParamComputableValueParamModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  void onTimeActivate( @Nonnull final ComputableValue<String> computableValue )
  {
  }
}
