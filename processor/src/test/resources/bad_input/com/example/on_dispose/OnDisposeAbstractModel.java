package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;

@ArezComponent
public abstract class OnDisposeAbstractModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  abstract void onMyValueDispose();
}
