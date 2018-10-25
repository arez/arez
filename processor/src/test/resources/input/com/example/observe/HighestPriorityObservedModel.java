package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighestPriorityObservedModel
{
  @Observe( priority = Priority.HIGHEST )
  protected void doStuff()
  {
  }
}
