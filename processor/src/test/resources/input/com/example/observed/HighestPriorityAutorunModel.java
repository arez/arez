package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighestPriorityAutorunModel
{
  @Observed( priority = Priority.HIGHEST )
  protected void doStuff()
  {
  }
}
