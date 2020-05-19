package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class DeprecatedMemoizeModel2
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @Deprecated
  @OnActivate
  void onTimeActivate()
  {
  }

  @OnDeactivate
  void onTimeDeactivate()
  {
  }
}
