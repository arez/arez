package com.example.on_deactivate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class OnDeactivateAbstractModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDeactivate
  abstract void onMyValueDeactivate();
}
