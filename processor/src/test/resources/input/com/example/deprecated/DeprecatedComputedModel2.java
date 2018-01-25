package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDispose;
import arez.annotations.OnStale;

@ArezComponent
public abstract class DeprecatedComputedModel2
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @Deprecated
  @OnActivate
  final void onTimeActivate()
  {
  }

  @OnDeactivate
  final void onTimeDeactivate()
  {
  }

  @OnStale
  final void onTimeStale()
  {
  }

  @OnDispose
  final void onTimeDispose()
  {
  }
}
