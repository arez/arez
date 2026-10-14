package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Executor;
import arez.annotations.Observe;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class MemoizeAndObserveMethodModel
{
  @Memoize
  @Observe( executor = Executor.EXTERNAL )
  public long getField()
  {
    return 22;
  }
}
