package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
abstract class OnActivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnActivate
  final void onTimeActivate()
  {
  }
}
