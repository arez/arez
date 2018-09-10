package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class HighPriorityAutorunModel
{
  @Observed( priority = Priority.HIGH )
  protected void doStuff()
  {
  }
}
