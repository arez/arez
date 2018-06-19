package com.example.autorun;

import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityAutorunModel
{
  @Autorun( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
