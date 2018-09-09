package com.example.computed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class HighPriorityComputedModel
{
  @Computed( priority = Priority.HIGH )
  public long getTime()
  {
    return 0;
  }
}
