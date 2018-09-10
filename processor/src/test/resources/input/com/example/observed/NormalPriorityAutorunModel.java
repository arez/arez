package com.example.observed;

import arez.annotations.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Observed;

@ArezComponent
public abstract class NormalPriorityAutorunModel
{
  @Observed( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
