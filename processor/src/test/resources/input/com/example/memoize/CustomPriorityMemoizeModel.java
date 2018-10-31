package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.Priority;

@ArezComponent
public abstract class CustomPriorityMemoizeModel
{
  @Computed( priority = Priority.LOWEST )
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
