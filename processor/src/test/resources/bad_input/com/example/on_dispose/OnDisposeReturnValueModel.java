package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;

@ArezComponent
public class OnDisposeReturnValueModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  int onMyValueDispose()
  {
    return 0;
  }
}
