package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnActivate;

@ArezComponent
public abstract class OnActivatePrivateModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  private void onMyValueActivate()
  {
  }
}
