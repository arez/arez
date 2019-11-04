package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnStale;

@ArezComponent
public abstract class MemoizeHasParametersModel
{
  @Memoize
  public int getMyValue( int x )
  {
    return 0;
  }

  @OnStale
  void onMyValueStale()
  {
  }
}
