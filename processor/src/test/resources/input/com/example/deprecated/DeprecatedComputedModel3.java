package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;

@ArezComponent
public abstract class DeprecatedComputedModel3
{
  @Computed
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
