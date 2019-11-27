package com.example.package_access.other;

import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

public abstract class BaseOnDeactivateModel
{
  @Memoize
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
}
