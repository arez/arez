package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;

@ArezComponent( deferSchedule = true )
abstract class ScheduleDeferredModel
{
  @Observe
  protected void doStuff()
  {
  }
}
