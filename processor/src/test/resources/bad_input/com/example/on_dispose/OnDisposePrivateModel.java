package com.example.on_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnDispose;

@ArezComponent
public class OnDisposePrivateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnDispose
  private void onMyValueDispose()
  {
  }
}
