package com.example.autorun;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class LowPriorityAutorunModel
{
  @Autorun( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
