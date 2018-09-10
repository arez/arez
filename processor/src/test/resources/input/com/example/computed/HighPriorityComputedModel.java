package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighPriorityComputedModel
{
  @Computed( priority = Priority.HIGH )
  public long getTime()
  {
    return 0;
  }
}
