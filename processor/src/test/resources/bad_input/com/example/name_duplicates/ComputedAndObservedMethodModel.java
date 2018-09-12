package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Executor;
import arez.annotations.Observed;

@ArezComponent
public abstract class ComputedAndObservedMethodModel
{
  @Computed
  @Observed( executor = Executor.APPLICATION )
  public long getField()
  {
    return 22;
  }
}
