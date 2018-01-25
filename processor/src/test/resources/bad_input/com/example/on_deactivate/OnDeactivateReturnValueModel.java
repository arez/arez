package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateReturnValueModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  int onMyValueDeactivate()
  {
    return 0;
  }
}
