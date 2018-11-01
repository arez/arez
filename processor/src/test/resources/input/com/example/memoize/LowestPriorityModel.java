package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityModel
{
  @Memoize( priority = Priority.LOWEST )
  public long getTime()
  {
    return 0;
  }
}
