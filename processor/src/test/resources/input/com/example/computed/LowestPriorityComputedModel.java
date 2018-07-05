package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityComputedModel
{
  @Computed( priority = Priority.LOWEST )
  public long getTime()
  {
    return 0;
  }
}
