package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighestPriorityComputedModel
{
  @Computed( priority = Priority.HIGHEST )
  public long getTime()
  {
    return 0;
  }
}
