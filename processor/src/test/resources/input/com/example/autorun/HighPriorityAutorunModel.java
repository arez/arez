package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class HighPriorityAutorunModel
{
  @Autorun( highPriority = true )
  protected void doStuff()
  {
  }
}
