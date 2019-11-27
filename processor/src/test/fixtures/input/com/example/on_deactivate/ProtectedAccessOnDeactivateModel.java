package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class ProtectedAccessOnDeactivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnDeactivate
  protected void onTimeDeactivate()
  {
  }
}
