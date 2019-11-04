package com.example.memoize;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import javax.annotation.Nonnull;

@ArezComponent
abstract class OnActivateWithRawComputableValueParamModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @SuppressWarnings( "rawtypes" )
  @OnActivate
  final void onTimeActivate( @Nonnull final ComputableValue computableValue )
  {
  }
}
