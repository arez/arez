package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnStale;

@ArezComponent
public abstract class OnStaleModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnStale
  final void onTimeStale()
  {
  }
}
