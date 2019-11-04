package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowPriorityObserveModel
{
  @Observe( priority = Priority.LOW )
  protected void doStuff()
  {
  }
}
