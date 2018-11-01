package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighPriorityModel
{
  @Memoize( priority = Priority.HIGH )
  public long getTime()
  {
    return 0;
  }
}
