package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class NormalPriorityComputedModel
{
  @Computed( priority = Priority.NORMAL )
  public long getTime()
  {
    return 0;
  }
}
