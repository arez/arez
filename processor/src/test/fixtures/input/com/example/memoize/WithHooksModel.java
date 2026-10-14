package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;
import arez.annotations.OnDeactivate;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class WithHooksModel
{
  @Memoize
  public long getTime()
  {
    return 0;
  }

  @OnActivate
  void onTimeActivate()
  {
  }

  @OnDeactivate
  void onTimeDeactivate()
  {
  }
}
