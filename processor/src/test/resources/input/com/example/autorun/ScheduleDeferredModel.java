package com.example.autorun;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent( deferSchedule = true )
public class ScheduleDeferredModel
{
  @Autorun
  public void doStuff()
  {
  }
}
