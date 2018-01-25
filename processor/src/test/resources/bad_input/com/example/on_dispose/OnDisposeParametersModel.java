package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;

@ArezComponent
public abstract class OnDisposeParametersModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  void onMyValueDispose( int x )
  {
  }
}
