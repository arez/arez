package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnStale;

@ArezComponent
public abstract class ComputedAndOnStaleMethodModel
{
  @Computed
  @OnStale
  public long getField()
  {
    return 22;
  }
}
