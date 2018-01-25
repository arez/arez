package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent( deferSchedule = true )
public abstract class ScheduleDeferredModel
{
  @Autorun
  public void doStuff()
  {
  }
}
