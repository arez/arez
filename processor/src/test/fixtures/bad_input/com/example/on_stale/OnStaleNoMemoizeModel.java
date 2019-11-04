package com.example.on_stale;

import arez.annotations.ArezComponent;
import arez.annotations.OnStale;

@ArezComponent
public abstract class OnStaleNoMemoizeModel
{
  @OnStale
  void onMyValueStale()
  {
  }
}
