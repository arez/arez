package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnStale;

@ArezComponent
public abstract class MemoizeAndOnStaleMethodModel
{
  @Memoize
  @OnStale
  public long getField()
  {
    return 22;
  }
}
