package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnStale;

@ArezComponent
public abstract class OnStaleBadNameModel3
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @OnStale( name = "-a-" )
  void foo()
  {
  }
}
