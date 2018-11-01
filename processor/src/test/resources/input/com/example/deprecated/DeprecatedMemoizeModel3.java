package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;

@ArezComponent
public abstract class DeprecatedMemoizeModel3
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

  @Deprecated
  @OnDeactivate
  final void onTimeDeactivate()
  {
  }

  @OnStale
  final void onTimeStale()
  {
  }
}