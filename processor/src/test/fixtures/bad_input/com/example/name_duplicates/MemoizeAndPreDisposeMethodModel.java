package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PreDispose;

@ArezComponent
public abstract class MemoizeAndPreDisposeMethodModel
{
  @Memoize
  @PreDispose
  public long getField()
  {
    return 22;
  }
}
