package com.example.package_access.other;

import arez.annotations.Computed;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import arez.annotations.OnStale;

public abstract class BaseOnActivateModel
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

  @OnDeactivate
  public final void onTimeDeactivate()
  {
  }

  @OnStale
  public final void onTimeStale()
  {
  }
}
