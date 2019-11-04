package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class ObserveLowerPriorityModel
{
  @Memoize( observeLowerPriorityDependencies = true )
  public long getTime()
  {
    return 0;
  }
}
