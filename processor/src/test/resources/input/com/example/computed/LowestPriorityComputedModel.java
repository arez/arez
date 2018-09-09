package com.example.computed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class LowestPriorityComputedModel
{
  @Computed( priority = Priority.LOWEST )
  public long getTime()
  {
    return 0;
  }
}
