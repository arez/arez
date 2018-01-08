package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnStale;

@ArezComponent
public class OnStaleStaticModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @OnStale
  static void onMyValueStale()
  {
  }
}
