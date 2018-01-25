package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class ComputedAndPreDisposeMethodModel
{
  @Computed
  @PreDispose
  public long getField()
  {
    return 22;
  }
}
