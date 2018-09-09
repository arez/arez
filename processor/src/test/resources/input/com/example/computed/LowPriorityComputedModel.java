package com.example.computed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class LowPriorityComputedModel
{
  @Computed( priority = Priority.LOW )
  public long getTime()
  {
    return 0;
  }
}
