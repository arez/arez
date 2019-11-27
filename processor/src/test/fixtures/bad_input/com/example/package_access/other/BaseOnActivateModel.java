package com.example.package_access.other;

import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

public abstract class BaseOnActivateModel
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
  public final void onTimeDeactivate()
  {
  }
}
