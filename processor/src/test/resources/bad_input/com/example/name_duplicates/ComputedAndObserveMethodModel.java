package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
public abstract class ComputedAndObserveMethodModel
{
  @Computed
  @Observe( executor = Executor.APPLICATION )
  public long getField()
  {
    return 22;
  }
}
