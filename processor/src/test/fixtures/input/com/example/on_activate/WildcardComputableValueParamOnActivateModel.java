package com.example.on_activate;

import arez.ComputableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import javax.annotation.Nonnull;

@ArezComponent
abstract class WildcardComputableValueParamOnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  final void onTimeActivate( @Nonnull final ComputableValue<?> computableValue )
  {
  }
}
