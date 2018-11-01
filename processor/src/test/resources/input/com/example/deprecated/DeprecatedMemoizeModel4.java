package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;

@ArezComponent
public abstract class DeprecatedMemoizeModel4
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @OnActivate
  final void onTimeActivate()
  {
  }

  @OnDeactivate
  final void onTimeDeactivate()
  {
  }

  @Deprecated
  @OnStale
  final void onTimeStale()
  {
  }
}
