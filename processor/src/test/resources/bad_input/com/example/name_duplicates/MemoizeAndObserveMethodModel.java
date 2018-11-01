package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class MemoizeAndObserveMethodModel
{
  @Memoize
  @Observe( executor = Executor.APPLICATION )
  public long getField()
  {
    return 22;
  }
}
