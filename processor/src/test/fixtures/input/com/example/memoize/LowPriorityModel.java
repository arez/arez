package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
abstract class LowPriorityModel
{
  @Memoize( priority = Priority.LOW )
  public long getTime()
  {
    return 0;
  }
}
