package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.OnDeactivate;

@ArezComponent
public abstract class MemoizeAndOnDeactivateMethodModel
{
  @Memoize
  @OnDeactivate
  public long getField()
  {
    return 22;
  }
}
