package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityObservedModel
{
  @Observe( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
