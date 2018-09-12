package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighPriorityObservedModel
{
  @Observed( priority = Priority.HIGH )
  protected void doStuff()
  {
  }
}
