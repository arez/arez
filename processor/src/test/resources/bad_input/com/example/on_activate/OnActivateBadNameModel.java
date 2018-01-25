package com.example.on_activate;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;

@ArezComponent
public abstract class OnActivateBadNameModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnActivate
  void foo()
  {
  }
}
