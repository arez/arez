package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent( deferSchedule = true )
abstract class ScheduleDeferredKeepAliveModel
{
  @Memoize( keepAlive = true )
  public long getTime()
  {
    return 0;
  }
}
