package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnDeactivate
  final void onTimeDeactivate()
  {
  }
}
