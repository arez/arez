package com.example.package_access.other;

import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseOnStaleModel
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
  public final void onTimeDeactivate()
  {
  }
}
