package com.example.computed;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public abstract class NormalPriorityComputedModel
{
  @Computed( priority = Priority.NORMAL )
  public long getTime()
  {
    return 0;
  }
}
