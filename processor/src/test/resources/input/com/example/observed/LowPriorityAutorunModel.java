package com.example.observed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class LowPriorityAutorunModel
{
  @Observed( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
