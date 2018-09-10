package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityComputedModel
{
  @Computed( priority = Priority.LOW )
  public long getTime()
  {
    return 0;
  }
}
