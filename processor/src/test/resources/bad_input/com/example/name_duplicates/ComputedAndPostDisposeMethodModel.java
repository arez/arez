package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostDispose;

@ArezComponent
public abstract class ComputedAndPostDisposeMethodModel
{
  @Computed
  @PostDispose
  public long getField()
  {
    return 22;
  }
}
