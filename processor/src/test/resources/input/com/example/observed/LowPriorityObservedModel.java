package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityObservedModel
{
  @Observed( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
