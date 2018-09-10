package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent( deferSchedule = true )
public abstract class ScheduleDeferredModel
{
  @Observed
  protected void doStuff()
  {
  }
}
