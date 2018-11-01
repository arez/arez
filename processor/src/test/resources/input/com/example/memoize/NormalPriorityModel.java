package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.Priority;

@ArezComponent
public abstract class NormalPriorityModel
{
  @Memoize( priority = Priority.NORMAL )
  public long getTime()
  {
    return 0;
  }
}
