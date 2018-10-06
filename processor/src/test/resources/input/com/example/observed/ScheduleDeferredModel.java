package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent( deferSchedule = true )
public abstract class ScheduleDeferredModel
{
  @Observe
  protected void doStuff()
  {
  }
}
