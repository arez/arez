package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class DeprecatedMemoizeModel3
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
}
