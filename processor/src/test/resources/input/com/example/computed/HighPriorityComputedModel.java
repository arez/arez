package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class HighPriorityComputedModel
{
  @Computed( highPriority = true )
  public long getTime()
  {
    return 0;
  }
}
