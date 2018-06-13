package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent( deferSchedule = true )
public abstract class ScheduleDeferredKeepAliveComputedModel
{
  @Computed( keepAlive = true )
  public long getTime()
  {
    return 0;
  }
}
