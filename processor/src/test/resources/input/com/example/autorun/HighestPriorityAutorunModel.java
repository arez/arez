package com.example.autorun;

import arez.Priority;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public abstract class HighestPriorityAutorunModel
{
  @Autorun( priority = Priority.HIGHEST )
  protected void doStuff()
  {
  }
}
