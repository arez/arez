package com.example.package_access.other;

import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnDispose;
import arez.annotations.OnStale;

public abstract class BaseOnDisposeModel
{
  @Computed
  public long getTime()
  {
    return 0;
  }

  @OnActivate
  public final void onTimeActivate()
  {
  }

  @OnDeactivate
  public final void onTimeDeactivate()
  {
  }

  @OnStale
  public final void onTimeStale()
  {
  }

  @OnDispose
  final void onTimeDispose()
  {
  }
}
