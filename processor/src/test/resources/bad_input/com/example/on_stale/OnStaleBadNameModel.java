package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnStale;

@ArezComponent
public abstract class OnStaleBadNameModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  void foo()
  {
  }
}
