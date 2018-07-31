package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
public abstract class CustomPriorityMemoizeModel
{
  @Memoize( priority = Priority.LOWEST )
  public long count( final long time, float someOtherParameter )
  {
    return time;
  }
}
