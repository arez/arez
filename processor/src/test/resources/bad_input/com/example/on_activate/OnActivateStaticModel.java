package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;

@ArezComponent
public class OnActivateStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  static void onMyValueActivate()
  {
  }
}
