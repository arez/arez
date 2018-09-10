package com.example.observed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class HighPriorityAutorunModel
{
  @Observed( priority = Priority.HIGH )
  protected void doStuff()
  {
  }
}
