package com.example.observed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class HighestPriorityAutorunModel
{
  @Observed( priority = Priority.HIGHEST )
  protected void doStuff()
  {
  }
}
