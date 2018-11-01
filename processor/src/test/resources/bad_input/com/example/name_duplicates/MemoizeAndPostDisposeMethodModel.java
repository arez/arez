package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostDispose;

@ArezComponent
public abstract class MemoizeAndPostDisposeMethodModel
{
  @Memoize
  @PostDispose
  public long getField()
  {
    return 22;
  }
}
