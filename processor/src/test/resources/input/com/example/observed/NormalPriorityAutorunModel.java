package com.example.observed;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.Priority;

@ArezComponent
public abstract class NormalPriorityAutorunModel
{
  @Observed( priority = Priority.NORMAL )
  protected void doStuff()
  {
  }
}
