package com.example.package_access.other;

import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;

public abstract class BaseOnDeactivateModel
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
  final void onTimeDeactivate()
  {
  }

  @OnStale
  public final void onTimeStale()
  {
  }
}
