package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
abstract class BasicOnDeactivateModel
{
  @Memoize
  long getTime()
  {
    return 0;
  }

  @OnDeactivate
  void onTimeDeactivate()
  {
  }
}
