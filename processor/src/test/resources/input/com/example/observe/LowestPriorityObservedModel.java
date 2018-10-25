package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
public abstract class LowestPriorityObservedModel
{
  @Observe( priority = Priority.LOWEST )
  protected void doStuff()
  {
  }
}
