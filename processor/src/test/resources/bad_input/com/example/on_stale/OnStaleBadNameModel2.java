package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnStale;

@ArezComponent
public abstract class OnStaleBadNameModel2
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale( name = "if" )
  void foo()
  {
  }
}
