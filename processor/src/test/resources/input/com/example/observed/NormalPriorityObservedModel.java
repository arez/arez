package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.Priority;

@ArezComponent
public abstract class NormalPriorityObservedModel
{
  @Observe( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
