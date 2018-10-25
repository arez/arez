package com.example.observe;

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
