package com.example.observed;

import arez.annotations.Observed;
import arez.annotations.Priority;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class LowestPriorityAutorunModel
{
  @Observed( priority = Priority.LOWEST )
  protected void doStuff()
  {
  }
}
