package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
abstract class HighestPriorityModel
{
  @Memoize( priority = Priority.HIGHEST )
  public long getTime()
  {
    return 0;
  }
}
