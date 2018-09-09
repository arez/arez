package com.example.autorun;

import arez.annotations.Priority;
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
