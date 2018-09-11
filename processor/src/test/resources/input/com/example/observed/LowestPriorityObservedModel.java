package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityObservedModel
{
  @Observed( priority = Priority.LOWEST )
  protected void doStuff()
  {
  }
}
