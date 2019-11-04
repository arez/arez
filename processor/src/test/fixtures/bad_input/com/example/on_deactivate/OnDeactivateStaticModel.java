package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateStaticModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  static void onMyValueDeactivate()
  {
  }
}
