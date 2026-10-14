package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class LowPriorityModel
{
  @Memoize( priority = Priority.LOW )
  public long getTime()
  {
    return 0;
  }
}
