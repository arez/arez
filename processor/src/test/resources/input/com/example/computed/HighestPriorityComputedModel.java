package com.example.computed;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class HighestPriorityComputedModel
{
  @Computed( priority = Priority.HIGHEST )
  public long getTime()
  {
    return 0;
  }
}
